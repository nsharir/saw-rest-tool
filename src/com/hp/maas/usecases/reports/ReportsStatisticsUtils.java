package com.hp.maas.usecases.reports;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.utils.chart.ChartUtils;
import com.hp.maas.utils.executers.multiTenant.TenantFilter;
import org.apache.commons.io.FileUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by sharir on 24/03/2015.
 */
public class ReportsStatisticsUtils {

    public static void grabStatisticsFromServer(String path, String month, Server serverOperator, Long fromTime) {

        File folder = new File(path);

        if (!folder.exists() || !folder.isDirectory()) {
            throw new RuntimeException("No such folder - " + path);
        }

        File[] files = folder.listFiles();
        for (File file : files) {
            file.delete();
        }

        ReportStatisticsDumper dumper = new ReportStatisticsDumper(path, month, fromTime);

        serverOperator.executeCommandMultiTenant(dumper, new TenantFilter() {
            @Override
            public boolean shouldRun(Tenant t) {
                return "v3".equals(t.getVersion());
            }
        });

        dumper.analyze();
    }

    public static void generateTrendReport(String path, Long from, boolean offline, boolean online) throws IOException {
        List<ReportMeasurement> all = readFromDisk(path);

        double exeTime = 0;
        double calcTime = 0;

        int total = 0;

        for (ReportMeasurement reportMeasurement : all) {
            if ((reportMeasurement.Offline && offline) || (!reportMeasurement.Offline && online)) {
                if (from == null || reportMeasurement.CalculationStartTime >= from) {
                    exeTime = exeTime + (reportMeasurement.ExecutionDuration);
                    calcTime = calcTime + (reportMeasurement.CalculationDuration);
                    total++;
                }
            }
        }

        System.out.println("Total executions: " + total);
        System.out.println("Average execution time: " + (exeTime / total));
        System.out.println("Average calculation time: " + (calcTime / total));



        TimeSeries onlineSeries = new TimeSeries("Online reports");
        TimeSeries offlineSeries = new TimeSeries("Offline reports");

        if (all.isEmpty()){
            System.out.println("No measurements found");
            return;
        }

        int TIME_BUCKET_SIZE = 100;


        Map<Long,Integer> timeBucketCountersOnline = new HashMap<Long, Integer>();
        Map<Long,Integer> timeBucketCountersOffline = new HashMap<Long, Integer>();

        for (ReportMeasurement one : all) {

            if ((one.Offline && !offline) || (!one.Offline && !online)) {
                continue;
            }

                long startTime = one.CalculationStartTime;
            long duration = one.CalculationDuration;

            if (from != null && from > startTime){
                continue;
            }

            int buckets = (int) (duration / TIME_BUCKET_SIZE);

            if ((duration % TIME_BUCKET_SIZE) != 0){
                buckets++;
            }

            long currentBucket = startTime - (startTime % TIME_BUCKET_SIZE);

            Map<Long,Integer> timeBucketCounters ;

            if (one.Offline){
                timeBucketCounters = timeBucketCountersOffline;
            }else{
                timeBucketCounters = timeBucketCountersOnline;
            }

            for (int i=0;i<buckets;i++){
                long key = currentBucket + i * TIME_BUCKET_SIZE;
                Integer counter = timeBucketCounters.get(key);
                if (counter == null){
                    counter = 0;
                }
                counter++;
                timeBucketCounters.put(key, counter);
            }
        }


        for (Map.Entry<Long, Integer> entry : timeBucketCountersOffline.entrySet()) {
            offlineSeries.add(new Millisecond(new Date(entry.getKey())), entry.getValue());
        }


        for (Map.Entry<Long, Integer> entry : timeBucketCountersOnline.entrySet()) {
            onlineSeries.add(new Millisecond(new Date(entry.getKey())), entry.getValue());
        }


        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(onlineSeries);
        dataset.addSeries(offlineSeries);

        JFreeChart timeSeriesChart = ChartFactory.createTimeSeriesChart("Reports Calculations (Total reports:"+total+" ,Average time: "+((int)(calcTime / total))+"ms  )", "Time", "Calculations", dataset);


        ChartUtils.openChartAsFile(timeSeriesChart);

    }

    private static List<ReportMeasurement> readFromDisk(String path) throws IOException {
        List<ReportMeasurement> all;
        File folder = new File(path);

        if (!folder.exists() || !folder.isDirectory()) {
            throw new RuntimeException("No such folder - " + path);
        }

        File dir = new File(path);
        File[] files = dir.listFiles();
        all = new ArrayList<ReportMeasurement>(files.length);
        for (File file : files) {
            if (file.isDirectory()){
                continue;
            }
            JSONArray array = new JSONArray(FileUtils.readFileToString(file));
            for (int i=0; i<array.length();i++) {
                all.add(ReportMeasurement.parse(array.getJSONObject(i)));
            }
        }
        return all;
    }
}

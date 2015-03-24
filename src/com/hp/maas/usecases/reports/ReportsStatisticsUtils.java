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

    public static void generateTrendReport(String path, Long from) throws IOException {
        List<ReportMeasurement> all = readFromDisk(path);

        double exeTime = 0;
        double calcTime = 0;

        int total = 0;

        for (ReportMeasurement reportMeasurement : all) {
            if (from == null || reportMeasurement.CalculationStartTime >= from) {
                exeTime = exeTime + (reportMeasurement.ExecutionDuration);
                calcTime = calcTime + (reportMeasurement.CalculationDuration);
                total++;
            }
        }

        System.out.println("Total executions: " + total);
        System.out.println("Average execution time: " + (exeTime / total));
        System.out.println("Average calculation time: " + (calcTime / total));


        List<ReportMeasurement> open = new ArrayList<ReportMeasurement>();

        Collections.sort(all, new Comparator<ReportMeasurement>() {
            @Override
            public int compare(ReportMeasurement o1, ReportMeasurement o2) {
                Long startTime1 = o1.CalculationStartTime;
                Long startTime2 = o2.CalculationStartTime;
                return startTime1.compareTo(startTime2);
            }
        });


        TimeSeries onlineSeries = new TimeSeries("Online reports");
        TimeSeries offlineSeries = new TimeSeries("Offline reports");


        for (ReportMeasurement one : all) {
            if (from == null || one.CalculationStartTime >= from) {

                open.add(one);
                List<ReportMeasurement> done = new ArrayList<ReportMeasurement>();
                for (ReportMeasurement oneOpen : open) {
                    if (one.CalculationStartTime > (oneOpen.CalculationStartTime + oneOpen.CalculationDuration)) {
                        done.add(oneOpen);
                    }
                }
                open.removeAll(done);

                int offline = 0;

                for (ReportMeasurement reportMeasurement : open) {
                    if (reportMeasurement.Offline) {
                        offline++;
                    }
                }
                onlineSeries.addOrUpdate(new Millisecond(new Date(one.CalculationStartTime)), open.size() - offline);
                offlineSeries.addOrUpdate(new Millisecond(new Date(one.CalculationStartTime)), offline);
            }

        }


        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(onlineSeries);
        dataset.addSeries(offlineSeries);

        JFreeChart timeSeriesChart = ChartFactory.createTimeSeriesChart("Reports Calculations (Total reports:"+total+" ,Average time: "+((int)(exeTime / total))+"ms  )", "Time", "Calculations", dataset);


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
            JSONArray array = new JSONArray(FileUtils.readFileToString(file));
            for (int i=0; i<array.length();i++) {
                all.add(ReportMeasurement.parse(array.getJSONObject(i)));
            }
        }
        return all;
    }
}

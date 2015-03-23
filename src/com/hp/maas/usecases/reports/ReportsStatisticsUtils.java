package com.hp.maas.usecases.reports;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import sun.org.mozilla.javascript.internal.json.JsonParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sharir on 23/03/2015.
 */
public class ReportsStatisticsUtils {

    public static List<ReportMeasurement> gatherStatistics(String folder){

        File dir = new File(folder);

        if (!dir.exists()){
            throw new RuntimeException("No such folder "+folder);
        }

        File[] files = dir.listFiles();

        List<ReportMeasurement> stats = new ArrayList<ReportMeasurement>(files.length);

        for (File file : files) {
            try {
                String s = FileUtils.readFileToString(file);
                JSONObject json = new JSONObject(s);

                ReportMeasurement reportMeasurement = new ReportMeasurement();

                reportMeasurement.id = json.getString("id");
                reportMeasurement.ReportId = json.getString("ReportId");
                reportMeasurement.Version = json.getString("Version");

                reportMeasurement.CalculationStartTime = json.getLong("CalculationStartTime");
                reportMeasurement.CalculationDuration = json.getLong("CalculationDuration");
                reportMeasurement.ExecutionStartTime = json.getLong("ExecutionStartTime");
                reportMeasurement.ExecutionDuration = json.getLong("ExecutionDuration");
                reportMeasurement.NumberOfResultRecords = json.getLong("NumberOfResultRecords");
                reportMeasurement.NumberOfResultBytes = json.getLong("NumberOfResultBytes");

                reportMeasurement.Offline = json.getBoolean("Offline");



                stats.add(reportMeasurement);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stats;
    }

    public static void calcAverages(String folder) {

        List<ReportMeasurement> reportMeasurements = ReportsStatisticsUtils.gatherStatistics(folder);

        double exeTime = 0;
        double calcTime = 0;

        for (ReportMeasurement reportMeasurement : reportMeasurements) {
            exeTime = exeTime + (reportMeasurement.ExecutionDuration/reportMeasurements.size());
            calcTime = calcTime + (reportMeasurement.CalculationDuration/reportMeasurements.size());
        }

        System.out.println("Total executions: "+reportMeasurements.size());
        System.out.println("Average execution time: "+exeTime);
        System.out.println("Average calculation time: "+calcTime);
    }
}

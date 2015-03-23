package com.hp.maas.usecases.reports;

import com.hp.maas.tools.FSOutput.FileSystemOutput;
import com.hp.maas.utils.executers.reporters.Reporter;

import java.io.IOException;
import java.util.List;

/**
 * Created by sharir on 23/03/2015.
 */
public class ReportStatisticsAverage extends ReportStatistics {

    public ReportStatisticsAverage(String month) {
        super(month);
    }


    protected void doAnalysis(List<ReportMeasurement> reportMeasurements){

            double exeTime = 0;
            double calcTime = 0;

            int total = 0;

            for (ReportMeasurement reportMeasurement : reportMeasurements) {
                if (shouldBeIncluded(reportMeasurement)) {
                    exeTime = exeTime + (reportMeasurement.ExecutionDuration);
                    calcTime = calcTime + (reportMeasurement.CalculationDuration);
                    total++;
                }
            }

            System.out.println("Total executions: "+total);
            System.out.println("Average execution time: "+(exeTime/total));
            System.out.println("Average calculation time: "+(calcTime/total));

    }

    protected boolean shouldBeIncluded(ReportMeasurement reportMeasurement){
        return true;
    }
}

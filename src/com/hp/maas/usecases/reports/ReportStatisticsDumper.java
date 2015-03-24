package com.hp.maas.usecases.reports;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by sharir on 23/03/2015.
 */
public class ReportStatisticsDumper extends ReportStatistics {


    private File dir;

    public ReportStatisticsDumper(String outputPath, String month, Long fromTime) {
        super(month, fromTime);
        this.dir = new File(outputPath);
        if (!this.dir.exists() && !this.dir.isDirectory()) {
            throw new RuntimeException("Folder doesn't exist - " + outputPath);
        }
    }


    protected void doAnalysis(List<ReportMeasurement> measurements) {
        JSONArray array = new JSONArray();
        for (ReportMeasurement instance : measurements) {
            array.put(instance.originalJSON);
        }

        try {
            String fileName = dir.getAbsolutePath() + File.separator + "ReportMeasurement_" + System.currentTimeMillis() + ".json";
            FileUtils.writeStringToFile(new File(fileName), array.toString(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

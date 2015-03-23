package com.hp.maas.usecases.reports;

import org.json.JSONObject;

/**
 * Created by sharir on 23/03/2015.
 */
public class ReportMeasurement {

    public String id;
    public String ReportId;
    public String Version;

    public long CalculationStartTime;
    public long CalculationDuration;

    public long ExecutionStartTime;
    public long ExecutionDuration;

    public long NumberOfResultRecords;
    public long NumberOfResultBytes;

    public boolean Offline;

    public JSONObject originalJSON;

    public static ReportMeasurement parse(JSONObject json) {
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

        reportMeasurement.originalJSON = json;

        return reportMeasurement;
    }

}

package com.hp.maas.usecases.reports;

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

}

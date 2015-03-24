package com.hp.maas.usecases.reports;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.query.APISyntaxFilterElement;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.apis.model.query.SimpleFilterElement;
import com.hp.maas.apis.model.rms.RMSInstance;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.tools.FSOutput.FileSystemOutput;
import com.hp.maas.utils.executers.multiTenant.TenantCommand;
import com.hp.maas.utils.executers.reporters.LogLevel;
import com.hp.maas.utils.executers.reporters.Reporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sharir on 23/03/2015.
 */
public abstract class ReportStatistics implements TenantCommand {


    /*
      {
         "id": "550f3a830cf26b7145237ddd",
         "Offline": false,
         "CalculationDuration": 46,
         "ReportId": "550f01220cf206b61acca673",
         "CalculationStartTime": 1427061379412,
         "ExecutionDuration": 47,
         "ExecutionStartTime": 1427061379411,
         "NumberOfResultRecords": 8,
         "Version": "-837814289",
         "NumberOfResultBytes": 4096
        }
     */


    private String month;
    private Long fromTime;
    private List<ReportMeasurement> measurements = new ArrayList<ReportMeasurement>();

    public ReportStatistics(String month, Long fromTime) {
        this.month = month;
        this.fromTime = fromTime;
    }

    @Override
    public void execute(Server server, Tenant tenant, Reporter reporter) {


        FilterBuilder filter = null;

        if (fromTime != null){
          filter = new FilterBuilder(new SimpleFilterElement("CalculationStartTime",">",fromTime));
        }

        List<RMSInstance> all = server.getRmsReaderAPI().readResources("ReportMeasurement"+month,filter);

        for (RMSInstance instance : all) {

            ReportMeasurement measurement = ReportMeasurement.parse(instance.getContent());
            measurements.add(measurement);
        }


    }

    public void analyze(){
        doAnalysis(measurements);
    }
    protected abstract void doAnalysis(List<ReportMeasurement> measurements);
}

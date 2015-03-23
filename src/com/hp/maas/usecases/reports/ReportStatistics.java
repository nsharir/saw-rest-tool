package com.hp.maas.usecases.reports;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.rms.RMSInstance;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.tools.FSOutput.FileSystemOutput;
import com.hp.maas.utils.executers.multiTenant.TenantCommand;
import com.hp.maas.utils.executers.reporters.LogLevel;
import com.hp.maas.utils.executers.reporters.Reporter;

import java.io.IOException;
import java.util.List;

/**
 * Created by sharir on 23/03/2015.
 */
public class ReportStatistics implements TenantCommand {


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


    String outputPath;
    String month;
    FileSystemOutput out;

    public ReportStatistics(String outputPath, String month) {
        this.outputPath = outputPath;
        this.month = month;
        this.out = new FileSystemOutput(outputPath);
    }

    @Override
    public void execute(Server server, Tenant tenant, Reporter reporter) {

        List<RMSInstance> all = server.getRmsReaderAPI().readResources("ReportMeasurement"+month);


        for (RMSInstance instance : all) {
            try {
                out.dump(/*tenant.getId()*/"","ReportMeasurement_"+instance.getContent().getString("id")+".json",instance.getContent().toString(1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

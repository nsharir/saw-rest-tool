package com.hp.maas.usecases.reports;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.rms.RMSInstance;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.tools.FSOutput.FileSystemOutput;
import com.hp.maas.utils.executers.multiTenant.TenantCommand;
import com.hp.maas.utils.executers.reporters.Reporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sharir on 23/03/2015.
 */
public  class ReportStatisticsDumper extends ReportStatistics {

    private FileSystemOutput out;



    public ReportStatisticsDumper(String outputPath, String month) {
        super(month);
        this.out = new FileSystemOutput(outputPath);
    }


    protected void doAnalysis(List<ReportMeasurement> measurements){
        for (ReportMeasurement instance : measurements) {
            try {
                out.dump(/*tenant.getId()*/"","ReportMeasurement_"+instance.originalJSON.getString("id")+".json",instance.originalJSON.toString(1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

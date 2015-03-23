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
public class PostUpgradeReportQueue implements TenantCommand {
    String outputPath;

    public PostUpgradeReportQueue(String outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public void execute(Server server, Tenant tenant, Reporter reporter) {

        List<RMSInstance> all = server.getRmsReaderAPI().readResources("WQPostUpgrade");

        FileSystemOutput out = new FileSystemOutput(outputPath);

        for (RMSInstance instance : all) {
            try {
                out.dump("",System.currentTimeMillis()+".json",instance.getContent().toString(1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        reporter.report(LogLevel.INFO,"Total queue size: "+all.size());
    }
}

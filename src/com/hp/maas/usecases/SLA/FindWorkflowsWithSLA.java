package com.hp.maas.usecases.SLA;

import com.hp.maas.apis.EntityReaderAPI;
import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.entity.EntityInstance;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.apis.model.query.SimpleFilterElement;
import com.hp.maas.apis.model.rms.RMSInstance;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.tools.FSOutput.FileSystemOutput;
import com.hp.maas.utils.executers.multiTenant.TenantCommand;
import com.hp.maas.utils.executers.reporters.LogLevel;
import com.hp.maas.utils.executers.reporters.Reporter;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by sharir on 19/02/2015.
 */
public class FindWorkflowsWithSLA implements TenantCommand{

    private FileSystemOutput out;

    public FindWorkflowsWithSLA(FileSystemOutput out) {
        this.out = out;
    }

    @Override
    public void execute(Server server, Tenant tenant, Reporter reporter) {
        List<RMSInstance> entityWorkflow = server.getRmsReaderAPI().readResources("EntityWorkflow");

        int invalidCounter = 0;

        for (RMSInstance rmsInstance : entityWorkflow) {

            String valid_workflows = tenant.getId()+ File.separator+"valid_workflows";
            String invalid_workflows = tenant.getId()+ File.separator+"invalid_workflows";

            JSONObject json = rmsInstance.getContent();
            String entityType = json.getString("EntityType");

            try {

                String jsonString = json.toString(1).replaceAll("setNextTargetTime","-");

                if (jsonString.contains("NextTargetTime") || jsonString.contains("SLTAggregatedStatus")){
                    invalidCounter++;
                    reporter.report(LogLevel.INFO, "[SLT_FIELD_USED_IN_WORKFLOW] entity="+entityType);
                    out.dump(invalid_workflows, entityType+".json", json.toString(1));
                }else{
                    //out.dump(valid_workflows, entityType+".json", json.toString(1));
                }

            } catch (IOException e) {
                reporter.report(LogLevel.ERROR, "Failed to write workflow to file for entity "+entityType);
            }
        }

        if (invalidCounter == 0){
            reporter.report(LogLevel.INFO,"Tenant is valid");
        }else{
            reporter.report(LogLevel.INFO,"Tenant is invalid "+invalidCounter+" workflow(s) are using SLT attributes ["+tenant+"]");
        }

    }
}

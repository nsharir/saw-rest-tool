package com.hp.maas.usecases.workflow.commands;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.apis.model.query.SimpleFilterElement;
import com.hp.maas.apis.model.rms.RMSInstance;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.utils.executers.multiTenant.TenantCommand;
import com.hp.maas.utils.executers.reporters.LogLevel;
import com.hp.maas.utils.executers.reporters.Reporter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by sharir on 20/05/2015.
 */
public class FindSLTOldRules implements TenantCommand {

    @Override
    public void execute(Server server, Tenant tenant, Reporter reporter) {
        List<RMSInstance> workflow = server.getRmsReaderAPI().readResources("EntityWorkflow",new FilterBuilder(new SimpleFilterElement("EntityType","=","Request")).or(new SimpleFilterElement("EntityType","=","Incident")));
        for (RMSInstance instance : workflow) {
            JSONArray ruleDefinitions = instance.getContent().getJSONArray("RuleDefinitions");

            for (int i=0;i<ruleDefinitions.length();i++){
                JSONObject o = (JSONObject) ruleDefinitions.get(i);
                String templateId = o.getString("TemplateId");
                if (templateId.equals("handleSltGoal")) {
                    reporter.report(LogLevel.WARN, " [" + server.getTenantId() + "]" + "[" + instance.getContent().getString("EntityType") + "][RuleId="+o.getString("id")+"]" );
                }
            }
        }

    }
}

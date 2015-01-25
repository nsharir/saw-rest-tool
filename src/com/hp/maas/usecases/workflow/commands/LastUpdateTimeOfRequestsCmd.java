package com.hp.maas.usecases.workflow.commands;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.utils.executers.multiTenant.TenantCommand;
import com.hp.maas.utils.executers.reporters.Reporter;
import org.json.JSONObject;

/**
 * Created by sharir on 25/01/2015.
 */
public class LastUpdateTimeOfRequestsCmd implements TenantCommand {
    @Override
    public void execute(Server server, Tenant tenant, Reporter reporter) {
        JSONObject object = server.getGenericRestAPI().executeGet("analytics/Request/aggregations?layout=max(LastUpdateTime)");
        int total = object.getJSONObject("meta").getInt("total_count");
        String maxDate = "NO DATA";
        if (total != 0) {
            JSONObject max = object.getJSONArray("Results").getJSONObject(0).getJSONObject("Aggregations").getJSONObject("max");
            try {
                maxDate = max.getString("LastUpdateTime");
            } catch (RuntimeException e) {
                maxDate = "NO DATA";
            }
        }
        System.out.println(tenant.getId() + "," + tenant.getType() + "," + maxDate);
    }
}

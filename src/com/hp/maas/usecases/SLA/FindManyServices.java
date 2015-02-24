package com.hp.maas.usecases.SLA;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.utils.executers.multiTenant.TenantCommand;
import com.hp.maas.utils.executers.reporters.LogLevel;
import com.hp.maas.utils.executers.reporters.Reporter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sharir on 24/02/2015.
 */
public class FindManyServices implements TenantCommand {
    @Override
    public void execute(Server server, Tenant tenant, Reporter reporter) {
        JSONObject jsonObject = server.getGenericRestAPI().executeGet("ems/Agreement?layout=DisplayLabel,SLAisActive,AgreementRegisteredForService.Id,AgreementRegisteredForService.DisplayLabel&filter=SLAisActive%3dtrue%20and%20AgreementRegisteredForService[]&meta=AllowM2MRelationsLayout");
//        System.out.println(jsonObject.toString());
//        System.out.println("\n");

        JSONArray entities = jsonObject.getJSONArray("entities");
        Map<String,JSONObject> servicesMap = new HashMap<String, JSONObject>();
        Map<String,JSONObject> agreementMap = new HashMap<String, JSONObject>();

        Map<String,List<String>> relationMap = new HashMap<String, List<String>>();

        for (int i=0;i<entities.length();i++){
            JSONObject relation = entities.getJSONObject(i);
            JSONObject agreement = relation.getJSONObject("properties");
            agreement.remove("LastUpdateTime");
            JSONObject service = relation.getJSONObject("related_properties").getJSONObject("AgreementRegisteredForService");

            String serviceId = service.getString("Id");
            servicesMap.put(serviceId,service);

            String agreementId = agreement.getString("Id");
            agreementMap.put(agreementId,agreement);

            List<String> agreements = relationMap.get(serviceId);
            if (agreements == null){
                agreements = new ArrayList<String>();
                relationMap.put(serviceId,agreements);
            }
            agreements.add(agreementId);

        }
        boolean isOk = true;
        for (Map.Entry<String, List<String>> entry : relationMap.entrySet()) {
            if (entry.getValue().size() > 1){
                List<JSONObject> many = new ArrayList<JSONObject>();
                for (String id : relationMap.get(entry.getKey())) {
                    many.add(agreementMap.get(id));
                }
                reporter.report(LogLevel.ERROR,"[MANY AGREEMENTS]: "+ servicesMap.get(entry.getKey())+" --> "+many);
                isOk = false;
            }
        }

        if (isOk) {
            reporter.report(LogLevel.INFO, "Tenant is OK. ["+tenant+"]");
        }else{
            reporter.report(LogLevel.INFO, "Tenant is not valid. ["+tenant+"]");
        }


    }
}

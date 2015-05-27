package com.hp.maas.usecases.SLA;

import com.hp.maas.apis.EntityReaderAPI;
import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.entity.EntityInstance;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.apis.model.query.SimpleFilterElement;
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
 * Created by sharir on 01/04/2015.
 */
public class FindBrokenServiceToSLAsDefinitions implements TenantCommand {

    @Override
    public void execute(Server server, Tenant tenant, Reporter reporter) {
        EntityReaderAPI reader = server.getEntityReaderAPI();
        List<EntityInstance> entityInstances = reader.readFullLayout("Agreement", new FilterBuilder(new SimpleFilterElement("DefaultSLA", "=", true)).and(new SimpleFilterElement("PhaseId", "=", "active")));
        if (entityInstances.size() > 1){
            reporter.report(LogLevel.ERROR, "Tenant is not valid. ["+tenant+"]");
            reporter.report(LogLevel.ERROR,"[TO_MANY_DEFAULTS] found:" );
            for (EntityInstance entityInstance : entityInstances) {
                reporter.report(LogLevel.INFO,entityInstance.toString());
            }
            return;
        }

        EntityInstance defaultSLA = null;
        if (entityInstances.isEmpty()){
            reporter.report(LogLevel.WARN,"[NO DEFAULT SLA] found:" );
        }else {
            defaultSLA = entityInstances.get(0);
        }

        JSONObject jsonObject = server.getGenericRestAPI().executeGet("ems/Agreement?layout=DisplayLabel,SLAisActive,AgreementRegisteredForService.Id,AgreementRegisteredForService.DisplayLabel,AgreementRegisteredForService.ActiveSLA&filter=SLAisActive%3dtrue%20and%20AgreementRegisteredForService[]&meta=AllowM2MRelationsLayout&size=4000");
//
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
        for (Map.Entry<String, List<String>> entry : relationMap.entrySet()) {
            List<String> agreements = entry.getValue();
            if (agreements.size() > 1){
                List<JSONObject> many = new ArrayList<JSONObject>();
                for (String id : relationMap.get(entry.getKey())) {
                    many.add(agreementMap.get(id));
                }
                reporter.report(LogLevel.ERROR,"[MANY AGREEMENTS]: "+ servicesMap.get(entry.getKey())+" --> "+many);
                reporter.report(LogLevel.ERROR, "Tenant is not valid. ["+tenant+"]");
                //return;
            }

            for (String agreement : agreements) {
                if (defaultSLA == null || !agreement.equals(defaultSLA.getFieldValue("Id"))) {
                    String serviceId = entry.getKey();
                    JSONObject serviceObj = servicesMap.get(serviceId);
                    String activeSLAId = null;
                    if (serviceObj.has("ActiveSLA")) {
                        activeSLAId = serviceObj.getString("ActiveSLA");
                    }
                    if (activeSLAId == null){
                        reporter.report(LogLevel.ERROR,"[ACTIVE SLA NOT DEFINED FOR SERVICE]: "+ "Service id: "+serviceId+ " is not mapped to agreement with id "+agreement);
                    }
                }
            }

            
        }

        



    }
}

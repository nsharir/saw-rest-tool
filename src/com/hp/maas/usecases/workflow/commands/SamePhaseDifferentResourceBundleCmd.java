package com.hp.maas.usecases.workflow.commands;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.metadata.EntityTypeDescriptor;
import com.hp.maas.apis.model.rb.ResourceBundleEntry;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.utils.executers.multiTenant.TenantCommand;
import com.hp.maas.utils.executers.reporters.Reporter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by sharir on 25/01/2015.
 */
public class SamePhaseDifferentResourceBundleCmd implements TenantCommand{
    @Override
    public void execute(Server server, Tenant tenant, Reporter reporter) {
        List<EntityTypeDescriptor> entityTypeDescriptors = server.getMetadataAPI().loadAllFromServer();
        for (EntityTypeDescriptor typeDescriptor : entityTypeDescriptors) {
            JSONObject jsonObject;
            try {
                jsonObject = server.getGenericRestAPI().executeGet("workflow/full/" + typeDescriptor.getName());
            }catch (RuntimeException e){
                continue;
            }

            Map<String,Set<String>> map = new HashMap<String, Set<String>>();

            JSONArray processes = jsonObject.getJSONArray("Processes");
            for (int p_i=0; p_i<processes.length();p_i++){
                JSONObject process = processes.getJSONObject(p_i);
                JSONArray phases = process.getJSONArray("Phases");
                for (int ph_i=0; ph_i<processes.length();ph_i++){
                    JSONObject phase = phases.getJSONObject(ph_i);
                    String id = phase.getString("id");
                    String displayLabelBundleKey = phase.getString("DisplayLabelBundleKey");

                    Set<String> strings = map.get(id);
                    if (strings == null){
                        strings = new HashSet<String>();
                        map.put(id, strings);
                    }

                    strings.add(displayLabelBundleKey);

                }
            }

            for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
                if (entry.getValue().size() > 1){
                    System.out.println(typeDescriptor.getName() + ": " + entry.getKey() + ": " + entry.getValue());
                    for (String bundleKey : map.keySet()) {
                        ResourceBundleEntry messages = server.getResourceBundleAPI().getResourceBundleEntry("workflow_resource_bundle_messages", bundleKey);
                        System.out.println(messages);
                    }
                }
            }
        }

    }
}

package com.hp.maas.usecases.forms;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.apis.model.query.SimpleFilterElement;
import com.hp.maas.apis.model.rb.ResourceBundleEntry;
import com.hp.maas.apis.model.rb.ResourceBundleParser;
import com.hp.maas.apis.model.rms.RMSInstance;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.tools.FSOutput.FileSystemOutput;
import com.hp.maas.utils.executers.multiTenant.TenantCommand;
import com.hp.maas.utils.executers.reporters.Reporter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by sharir on 09/06/2015.
 */
public class ServiceRequestFormDescriptionLabelFix implements TenantCommand {
    private String outputDir;
    private String newLabel;

    public ServiceRequestFormDescriptionLabelFix(String outputDir, String newLabel) {
        this.outputDir = outputDir;
        this.newLabel = newLabel;
    }

    @Override
    public void execute(Server s, Tenant tenant, Reporter reporter) {

        FileSystemOutput out = new FileSystemOutput(outputDir);

        ResourceBundleEntry entry = s.getResourceBundleAPI().getResourceBundleEntry("saw_metadata_messages", "serviceRequest.description");

        try {
            out.dump("","saw_metadata_messages.rb.json", ResourceBundleParser.entryToJson(entry));
        } catch (IOException e) {
            e.printStackTrace();
        }


        entry.setLabel("en",newLabel);
        entry.setLabel("en-GB",newLabel);

        try {
            out.dump("","saw_metadata_messages.rb_fixed.json", ResourceBundleParser.entryToJson(entry));
        } catch (IOException e) {
            e.printStackTrace();
        }

        s.getResourceBundleAPI().updateResourceBundleEntry(entry);

/*        List<RMSInstance> formLayout = server.getRmsReaderAPI().readResources("formLayout", new FilterBuilder(new SimpleFilterElement("name", "=", "serviceRequest")).and(new SimpleFilterElement("entityType", "=", "Request")));

        RMSInstance rmsInstance = formLayout.get(0);
        JSONObject json = rmsInstance.getContent();


        FileSystemOutput out = new FileSystemOutput(outputDir);
        try {
            out.dump("","serviceRequest_original.json",json.toString(1));
        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONArray jsonArray = json.getJSONArray("sections").getJSONObject(0).getJSONArray("fields");

        for (int i=0;i<jsonArray.length();i++){
            JSONObject field = jsonArray.getJSONObject(i);
            if (field.getString("modelAttribute").equals("Description")){
                field.put("resourceKey","serviceRequest.description");
            }
        }

        try {
            out.dump("","serviceRequest_fixed.json",json.toString(1));
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.getRmsWriterAPI().updateResource(rmsInstance);*/
    }
}

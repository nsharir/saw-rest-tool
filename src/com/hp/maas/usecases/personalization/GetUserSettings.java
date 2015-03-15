package com.hp.maas.usecases.personalization;

import com.hp.maas.apis.Server;
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
 * Created by sharir on 02/03/2015.
 */
public class GetUserSettings implements TenantCommand {

    FileSystemOutput out;
    public GetUserSettings(String outputPath) {
        out = new FileSystemOutput(outputPath);
    }

    @Override
    public void execute(Server server, Tenant tenant, Reporter reporter) {
        int total = 0;
        int highContrast = 0;

        List<RMSInstance> personalizationSetting = server.getRmsReaderAPI().readResources("personalizationSetting");
        for (RMSInstance instance : personalizationSetting) {
            total++;
            JSONObject json = instance.getContent();

            if (!json.has("systemSettings")){
                continue;
            }
            JSONArray jsonArray = json.getJSONArray("systemSettings");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                if (object.has("key")){
                    if (!object.getString("key").equals("highContrastMode")){
                        continue;
                    }
                    if (object.has("value")){
                        if (object.getString("value").equals("true")){
                            highContrast++;
                        }
                    }
                }
            }
        }


        try {
            out.dump("","all.log",highContrast+"/"+total+"   ["+tenant+"]\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

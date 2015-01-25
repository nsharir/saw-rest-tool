package com.hp.maas.usecases.notificationTemplates;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.apis.model.query.SimpleFilterElement;
import com.hp.maas.apis.model.rms.RMSInstance;
import com.hp.maas.tools.FSOutput.FileSystemOutput;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by sharir on 25/01/2015.
 */
public class CommentsNotificationTemplatesUpdater {

    private Server server;
    private String outputFolder;

    public CommentsNotificationTemplatesUpdater(Server server, String outputFolder) {
        this.server = server;
        this.outputFolder = outputFolder;
    }

    public void run(boolean simulate) throws IOException{
        List<RMSInstance> rmsInstances = server.getRmsReaderAPI().readResources("NotificationTemplateBody", new FilterBuilder(new SimpleFilterElement("name", "=", "CommentsAddCommentTemplate")).or(new SimpleFilterElement("name","=","CommentsUpdateCommentTemplate")));

        File target = new File(outputFolder +server.getTenantId());
        target.mkdirs();

        FileSystemOutput output = new FileSystemOutput(target.getAbsolutePath());

        for (RMSInstance instance : rmsInstances) {

            String locale = instance.getContent().getString("locale");

            if (!"en".equals(locale)){
                continue;
            }

            String info =instance.getContent().getString("name")+"_"+ locale;

            output.dump("templates",info+".json",instance.getContent().toString(1));

            JSONObject content = instance.getContent().getJSONObject("notification_content").getJSONObject("mail_notification_content");
            content.put("from_label","IT Support Idemitsu");
            content.put("from_email", "it.helpdesk.saw@idemitsu.no");

            if (instance.getContent().getString("locale").equals("en")){
                content.put("subject","Your ${entity.type} ${entity.id} has been updated");
            }

            output.dump("templates",info+"_fixed.json",instance.getContent().toString(1));

            if (!simulate) {
                server.getRmsWriterAPI().updateResource(instance);
            }
        }
    }
}

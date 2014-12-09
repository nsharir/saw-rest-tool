package com.hp.maas.usecases.forms;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.rb.ResourceBundleEntry;
import com.hp.maas.apis.model.rms.RMSInstance;
import com.hp.maas.jsons.forms.Form;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sharir on 09/12/2014.
 */
public class FormsRealTransaction implements FormsTransaction {


    private Server server;
    private File targetDir;
    private boolean simulationMode;
    private Form toDelete;
    private Form newForm;
    private Map<String, String> resourceBundles = new HashMap<String, String>();

    public FormsRealTransaction(Server server , File targetDir , boolean simulationMode) {
        this.server = server;
        this.targetDir = targetDir;
        this.simulationMode = simulationMode;
    }

    @Override
    public void delete(Form form) {
        toDelete = form;
    }

    @Override
    public void update(Form form) {
        newForm = form;
    }

    @Override
    public void commit() {
       if (newForm != null && newForm.getSections().isEmpty()){
           toDelete = newForm;
       }

       if (toDelete != null){
           doDelete();
           if (!simulationMode){
               server.getRmsWriterAPI().deleteResource(new RMSInstance("formLayout", toDelete.getOriginalFormJson()));
           }
           return;
       }

       if (!resourceBundles.isEmpty()){
           createBundles();

           List<String> supportedLocales = server.getResourceBundleAPI().getSupportedLocales();

           if (!simulationMode){
               for (Map.Entry<String, String> entry : resourceBundles.entrySet()) {
                   Map<String, String> values = new HashMap<String, String>();
                   for (String locale : supportedLocales) {
                       values.put(locale,entry.getValue());
                   }
                   ResourceBundleEntry rb = new ResourceBundleEntry("customization_metadata_messages",entry.getKey(),values);
                   try {
                       server.getResourceBundleAPI().insertResourceBundleEntry(rb);
                   }catch (Exception e){
                       server.getResourceBundleAPI().updateResourceBundleEntry(rb);
                   }

               }
           }
       }
       if (newForm != null){
           storeForm(newForm);
           if (!simulationMode){
               server.getRmsWriterAPI().updateResource(new RMSInstance("formLayout", newForm.getOriginalFormJson()));
           }
       }
    }

    private void doDelete() {

        if (!targetDir.exists()){
            targetDir.mkdirs();
        }
        String formName = toDelete.getEntityType()+"_"+toDelete.getName().replaceAll(" ","_")+"_deleted.json";
        File formFile = new File(targetDir.getAbsoluteFile()+File.separator+formName);

        try {
            FileUtils.writeStringToFile(formFile, "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createBundles() {
        if (!targetDir.exists()){
            targetDir.mkdirs();
        }
        String formName = "_bundle_fixes_.json";
        File formFile = new File(targetDir.getAbsoluteFile()+File.separator+formName);

        StringBuilder string = new StringBuilder();

        for (Map.Entry<String, String> entry : resourceBundles.entrySet()) {
            string.append(entry.getKey()+"="+entry.getValue()).append("\n");
        }

        try {
            FileUtils.writeStringToFile(formFile, string.toString(),true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createRB(String resourceKey, String label) {
        resourceBundles.put(resourceKey, label);
    }

    private void storeForm(Form form){

        if (!targetDir.exists()){
            targetDir.mkdirs();
        }
        String formName = form.getEntityType()+"_"+form.getName().replaceAll(" ","_")+"_fixed.json";
        File formFile = new File(targetDir.getAbsoluteFile()+File.separator+formName);

        try {
            FileUtils.writeStringToFile(formFile, form.getOriginalFormJson().toString(1) + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

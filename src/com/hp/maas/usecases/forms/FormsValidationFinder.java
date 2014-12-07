package com.hp.maas.usecases.forms;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.metadata.EntityTypeDescriptor;
import com.hp.maas.apis.model.metadata.FieldDescriptor;
import com.hp.maas.apis.model.rms.RMSInstance;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.jsons.forms.Form;
import com.hp.maas.jsons.forms.FormField;
import com.hp.maas.jsons.forms.FormParser;
import com.hp.maas.jsons.forms.FormSection;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sharir on 27/11/2014.
 */
public class FormsValidationFinder {

    String serverUrl;
    String ssoToken;

    File outputFolder;
    File globalLog;
    File tenantDir;
    File tenantLog = null;

    List<Tenant> tenants;

    public FormsValidationFinder(String serverUrl, String ssoToken, String outputFolderPath , String operatorTenantId) {
        this.serverUrl = serverUrl;
        this.ssoToken = ssoToken;
        init(outputFolderPath,operatorTenantId);
    }


    private void init(String outputFolderPath,String operatorTenantId) {

        outputFolder = new File(outputFolderPath);
        if (!outputFolder.exists()){
            throw new RuntimeException("No such output path "+outputFolderPath);
        }

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy__HH-mm-ss");
        String executionFolder = outputFolder.getAbsolutePath()+File.separator+format.format(new Date());
        outputFolder = new File(executionFolder);

        outputFolder.mkdirs();

        globalLog = new File(outputFolder.getAbsolutePath()+File.separator+"All.log");


        createTenantList(operatorTenantId);

    }

    private void createTenantList(String operatorTenantId) {

        Server server = new Server(serverUrl, operatorTenantId,ssoToken);
        server.authenticate();


        List<Tenant> allTenants = server.getTenantManagementAPI().getAllTenants();

        tenants = new ArrayList<Tenant>();

        for (Tenant t : allTenants) {
            if (!t.getId().equals(operatorTenantId) && "Active".equals(t.getState())) {
                tenants.add(t);
            }
        }
    }

    public void run(){
        reportGlobal("Configuration: ");
        reportGlobal("{" +
                "serverUrl='" + serverUrl + '\n' +
                ", outputFolder='" + outputFolder + '\n' +
                ", tenants=" + tenants.size() +
                "}\n");
        reportGlobal("------------------------------------------------------------------------------");

        for (Tenant t : tenants) {
            try {
                runTenant(t);
            }catch(Throwable e){
                reportGlobal("Error while validating tenant "+t);
                System.out.println(e);
            }
            reportGlobal("------------------------------------------------------------------------------");
        }
    }


    private void runTenant(Tenant tenant){

        reportGlobal("** Validating tenant " +tenant);
        long time = System.currentTimeMillis();

        tenantDir = new File(outputFolder.getAbsolutePath()+File.separator+tenant.getId());
        tenantLog = null;

        Server server = new Server(serverUrl, tenant.getId(),ssoToken);
        server.authenticate();

        server.getMetadataAPI().loadAllFromServer();

        List<RMSInstance> rmsInstances = server.getRmsReaderAPI().readResources("formLayout");


        List<String> log = new ArrayList<String>();


        List<Form> forms = new ArrayList<Form>();

        for (RMSInstance rmsInstance : rmsInstances) {
            forms.add(FormParser.toForm(rmsInstance.getContent()));
        }

        int invalid = 0;

        for (Form form : forms) {
            boolean b = validateForm(server,form,log);
            if (!b){
                invalid++;
            }
        }

        long elapsed =  System.currentTimeMillis() - time;

        if (invalid == 0) {
            reportGlobal("** Done validating tenant " + server.getTenantId() + " - all " + forms.size() + " forms are valid. ("+elapsed+"ms )");
        }else{
            String msg = "** Done validating tenant " + server.getTenantId() + " - " + invalid + " out of " + forms.size() + " forms failed on errors. ("+elapsed+"ms )";
            report(log);
            reportGlobal(msg);
            report(msg);
        }

    }

    private boolean validateForm(Server server,Form form , List<String> log) {

        List<String> errors = new ArrayList<String>();

        EntityTypeDescriptor descriptor = null;

        try{
            descriptor = server.getMetadataAPI().getEntityDescriptor(form.getEntityType());
        }catch (Exception e){
            //do nothing.
        }

        if (descriptor == null){
            errors.add("["+form.getEntityType()+"] is not a valid entity type");
        }else {

            for (FormSection section : form.getSections()) {
                HashSet<String> badField = new HashSet<String>();

                boolean badHeader = false;

                if (section.getName() == null){
                    badHeader = true;
                }

                if (section.getResourceKey() == null){
                    badHeader = true;
                }

                if (section.getDomain() == null){
                    //we only allow empty domain for customization keys
                    if (!(section.getResourceKey() != null && section.getResourceKey().contains("customization"))) {
                        badHeader = true;
                    }
                }

                if (badHeader){
                    String details = "{" +
                            "name='" + section.getName() + '\'' +
                            ", header='" + section.getHeader() + '\'' +
                            ", resourceKey='" + section.getResourceKey() + '\'' +
                            ", domain='" + section.getDomain() + '\''+
                            "}";

                    errors.add("BAD_HEADER: " +" [entityType=" + form.getEntityType() + "]"+"[form=" + form.getName() + "][section=" + (section.getHeader() != null ? section.getHeader() : section.getName()) + "] section has a bad header. "+details);
                }

                for (FormField field : section.getFields()) {

                    if (field.getEditorType().equals("MORE")){
                        continue;
                    }
                    FieldDescriptor md = descriptor.getField(field.getModelAttribute());
                    if (md == null) {
                        if (descriptor.getRelation(field.getModelAttribute()) == null) {
                            errors.add("MISSING_IN_MD: "+" [entityType=" + form.getEntityType() + "]"+"[form=" + form.getName() + "][field" + field.getModelAttribute() + "] doesn't exist in metadata of entity type " + form.getEntityType() + " (section = " + section.getName() + ")");
                            badField.add(field.getModelAttribute());
                        }
                    }else{
                        if (md.isHidden()){
                            errors.add("HIDDEN_FIELD: "+" [entityType=" + form.getEntityType() + "]"+"[form=" + form.getName() + "][field=" + field.getModelAttribute() + "] is define as hidden field in entity type " + form.getEntityType() + " (section = " + section.getName() + ")");
                            badField.add(field.getModelAttribute());
                        }
                    }
                }

                if (section.getFields().isEmpty() || ((section.getFields().size() - badField.size()) == 0)) {
                    errors.add("NO_SECTION: "+" [entityType=" + form.getEntityType() + "]"+"[form=" + form.getName() + "][section="+section.getName()+"] Section has no fields (or all of his fields are invalid)");
                }


            }
        }


        if (errors.isEmpty()){
            //log.add(" Form [" + form.getName() + "]" + " [entityType=" + form.getEntityType() + "] was verified successfully.");
            //storeForm(form);
        }
        else {
            //log.add("ERROR: Form [" + form.getName() + "]" + " [entityType=" + form.getEntityType() + "] failed on some validations (" + errors.size() + " failures ):");
            log.addAll(errors);
            storeForm(form);
        }


        return errors.isEmpty();

    }



    private void report(String str) {
        List<String> list = new ArrayList<String>();
        list.add(str);
        report(list);
    }

    private void report(List<String> strings) {
        for (String text : strings) {
            System.out.println(text);
        }
        try {
            FileUtils.writeStringToFile(getTenantLogFile(), "", true);
            FileUtils.writeLines(getTenantLogFile(), strings, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void reportGlobal(String text){
        System.out.println(text);
        try {
            FileUtils.writeStringToFile(globalLog,text+"\n",true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void storeForm(Form form){
        if (!tenantDir.exists()){
            tenantDir.mkdirs();
        }
        String formName = form.getEntityType()+"_"+form.getName().replaceAll(" ","_")+".json";
        File formFile = new File(tenantDir.getAbsoluteFile()+File.separator+formName);

        try {
            FileUtils.writeStringToFile(formFile, form.getOriginalFormJson().toString(1)+"\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private  File getTenantLogFile(){

        if (tenantLog == null){
            tenantLog = new File(tenantDir.getAbsoluteFile()+File.separator+"_tenant.log");
            tenantDir.mkdirs();
        }

        return tenantLog;

    }

}

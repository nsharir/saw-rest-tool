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
import com.hp.maas.utils.executers.multiTenant.TenantFilter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sharir on 27/11/2014.
 */
public class FormsValidationFinder {


    File outputFolder;
    File globalLog;
    File tenantDir;
    File tenantLog = null;

    List<Tenant> tenants;
    private boolean simulationMode;
    private Server operationTenantServer;

    public FormsValidationFinder(Server operationTenantServer, String outputFolderPath , String operatorTenantId , TenantFilter filter) {
        this.operationTenantServer = operationTenantServer;
        init(outputFolderPath,operatorTenantId,filter);
    }


    private void init(String outputFolderPath, String operatorTenantId, TenantFilter tenantsFilter) {

        outputFolder = new File(outputFolderPath);
        if (!outputFolder.exists()){
            throw new RuntimeException("No such output path "+outputFolderPath);
        }

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy__HH-mm-ss");
        String executionFolder = outputFolder.getAbsolutePath()+File.separator+format.format(new Date());
        outputFolder = new File(executionFolder);

        outputFolder.mkdirs();

        globalLog = new File(outputFolder.getAbsolutePath()+File.separator+"All.log");


        createTenantList(operatorTenantId,tenantsFilter);

    }

    private void createTenantList(String operatorTenantId, TenantFilter tenantsFilter) {

        Server server = operationTenantServer.fork(operatorTenantId);


        List<Tenant> allTenants = server.getTenantManagementAPI().getAllTenants();

        tenants = new ArrayList<Tenant>();

        for (Tenant t : allTenants) {
            if (!t.getId().equals(operatorTenantId) && "Active".equals(t.getState())) {
                if (tenantsFilter == null || tenantsFilter.shouldRun(t)) {
                    tenants.add(t);
                }
            }
        }
    }

    public void run(){
        run(false);
    }

    public void simulate(){
        run(true);
    }

    private void run(boolean simulationMode){
        this.simulationMode = simulationMode;
        reportGlobal("Configuration: ");
        reportGlobal("{" +
                "serverUrl='" + operationTenantServer.getServerUrl() + '\n' +
                ", outputFolder='" + outputFolder + '\n' +
                ", tenants=" + tenants.size() +
                "}\n");
        reportGlobal("------------------------------------------------------------------------------");

        int total = tenants.size();
        int current = 0;

        for (Tenant t : tenants) {
            current++;
            reportGlobal("["+current+"/"+total+"]"+"Running tenant "+t.getTenantName()+"...");
            try {
                runTenant(t);
            }catch(Throwable e){
                reportGlobal("Error while validating tenant "+t);
                e.printStackTrace();
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

        Server server = operationTenantServer.fork(tenant.getId());

        server.getMetadataAPI().loadAllFromServer();

        List<RMSInstance> rmsInstances = server.getRmsReaderAPI().readResources("formLayout");


        List<String> log = new ArrayList<String>();


        List<Form> forms = new ArrayList<Form>();

        for (RMSInstance rmsInstance : rmsInstances) {
            forms.add(FormParser.toForm(rmsInstance.getContent()));
        }

        Map<Form,List<FormError>> errorsMap = new HashMap<Form, List<FormError>>();

        for (Form form : forms) {
            List<FormError> formErrors = validateForm(server, form, log);
            if (!formErrors.isEmpty()) {
                errorsMap.put(form, formErrors);
            }
        }

        long elapsed =  System.currentTimeMillis() - time;

        int invalid = errorsMap.keySet().size();

        if (invalid == 0) {
            reportGlobal("** Done validating tenant " + server.getTenantId() + " - all " + forms.size() + " forms are valid. ("+elapsed+"ms )");
        }else{
            String msg = "** Done validating tenant " + server.getTenantId() + " - " + invalid + " out of " + forms.size() + " forms failed on errors. ("+elapsed+"ms )";
            report(log);
            reportGlobal(msg);
            report(msg);

            reportGlobal("** Fixing tenant... " + server.getTenantId());
            report("** Fixing tenant... " + server.getTenantId());

            for (Form form : errorsMap.keySet()) {
                List<FormError> formErrors = errorsMap.get(form);

                FormsTransaction tx = new FormsRealTransaction(server, tenantDir,simulationMode) ;

                for (FormError error : formErrors) {
                    error.fix(tx);
                    String fixMsg = error.getFixMessage() ;
                    reportGlobal(fixMsg);
                    report(fixMsg);
                }

                tx.commit();
            }
        }



    }

    private List<FormError> validateForm(Server server,Form form , List<String> log) {

        List<FormError> errors = new ArrayList<FormError>();

        EntityTypeDescriptor descriptor = null;

        try{
            descriptor = server.getMetadataAPI().getEntityDescriptor(form.getEntityType());
        }catch (Exception e){
            //do nothing.
        }

        if (descriptor == null){
            errors.add(new InvalidEntityTypeFormError(form));
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
                    errors.add(new InvalidSectionHeaderFormError(form, section));
                }

                for (FormField field : section.getFields()) {

                    if (field.getEditorType().equals("MORE")){
                        continue;
                    }
                    FieldDescriptor md = descriptor.getField(field.getModelAttribute());
                    if (md == null) {
                        if (descriptor.getRelation(field.getModelAttribute()) == null) {
                            errors.add(new InvalidFieldFormError(form, section, field, "INVALID MD"));
                            badField.add(field.getModelAttribute());
                        }
                    }else{
                        if (md.isHidden()){
                            errors.add(new InvalidFieldFormError(form, section, field, "HIDDEN FIELD"));
                            badField.add(field.getModelAttribute());
                        }
                    }
                }

                if (section.getFields().isEmpty() || ((section.getFields().size() - badField.size()) == 0)) {
                    errors.add(new InvalidSectionFormError(form, section));
                }


            }
        }


        if (!errors.isEmpty()){
            //log.add("ERROR: Form [" + form.getName() + "]" + " [entityType=" + form.getEntityType() + "] failed on some validations (" + errors.size() + " failures ):");
            List<String> errStrings = new ArrayList<String>();

            for (FormError error : errors) {
                errStrings.add(error.getErrorMessage());
            }
            log.addAll(errStrings);
            storeForm(form);
        }


        return errors;

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

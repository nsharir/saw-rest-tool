package com.hp.maas.usecases.URLActions;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.rms.RMSInstance;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.jsons.forms.Form;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sharir on 27/11/2014.
 */
public class URLActionsGetter {

    String serverUrl;
    String ssoToken;

    File outputFolder;
    File globalLog;
    File tenantDir;
    File tenantLog = null;

    List<Tenant> tenants;

    public URLActionsGetter(String serverUrl, String ssoToken, String outputFolderPath, String operatorTenantId) {
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

        reportGlobal("** Reading tenant " +tenant);
        long time = System.currentTimeMillis();

        tenantDir = new File(outputFolder.getAbsolutePath()+File.separator+tenant.getId());
        tenantLog = null;

        Server server = new Server(serverUrl, tenant.getId(),ssoToken);
        server.authenticate();

        List<RMSInstance> rmsInstances = server.getRmsReaderAPI().readResources("customAction");


        for (RMSInstance instance : rmsInstances) {
            storeInstance(instance);
        }

        long elapsed =  System.currentTimeMillis() - time;

        reportGlobal("** Done reading tenant " + server.getTenantId() + " - " + rmsInstances.size() + " actions found. (" + elapsed + "ms )");

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


    private void storeInstance(RMSInstance instance){
        if (!tenantDir.exists()){
            tenantDir.mkdirs();
        }
        String type;
        if (!instance.getContent().keySet().contains("entityType")){
            type = "unknown";
        }   else{

            type = instance.getContent().getString("entityType");
        }

        File formFile = new File(tenantDir.getAbsoluteFile()+File.separator+ type +"_"+System.currentTimeMillis()+".json");

        try {
            FileUtils.writeStringToFile(formFile, instance.getContent().toString(1)+"\n");
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

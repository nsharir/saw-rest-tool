package com.hp.maas;

import com.hp.maas.apis.*;
import com.hp.maas.apis.model.entity.EntityBulkResult;
import com.hp.maas.apis.model.entity.EntityInstance;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.apis.model.query.SimpleFilterElement;
import com.hp.maas.apis.model.rms.RMSInstance;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.tools.FSOutput.FileSystemOutput;
import com.hp.maas.utils.Log;
import com.hp.maas.utils.executers.multiTenant.MultiTenantExecutor;
import com.hp.maas.utils.executers.multiTenant.TenantCommand;
import com.hp.maas.utils.executers.multiTenant.TenantFilter;
import com.hp.maas.utils.executers.reporters.ConsoleReporter;
import com.hp.maas.utils.executers.reporters.LogLevel;
import com.hp.maas.utils.executers.reporters.Reporter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 9:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReleaseScript {

    private static Reporter reporter;

    public static void main(String[] args){

        if(args.length < 5) {
            Log.error("Wrong parameters");
            return;
        }
        String url = args[0];
        String operUserName = args[1];
        String operPassword = args[2];
        String operTenantID = args[3];
        String outputFolder = args[4];

        TenantFilter filter = new TenantFilter() {
            @Override
            public boolean shouldRun(Tenant t) {
                return true;
            }
        };

        final FileSystemOutput output = new FileSystemOutput(outputFolder);


        reporter = new Reporter() {
            @Override
            public void report(LogLevel level, String str) {
                try {
                    output.dump(null,"all.log",str);
                    if (level == LogLevel.ERROR){
                        output.dump(null,"errors.log",str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        MultiTenantExecutor exe = new MultiTenantExecutor(url, operUserName, operPassword, operTenantID, filter,new ConsoleReporter());

        exe.run(new TenantCommand() {
            @Override
            public void execute(Server server, Tenant tenant, Reporter reporter) {
                try {
                    insertReleaseApprovalPlan(server);
                } catch (Exception e) {
                    reporter.report(LogLevel.ERROR,"Failed to insert release approval plan to system" );
                    reporter.report(LogLevel.ERROR, ExceptionUtils.getStackTrace(e));
                }
                try {
                    insertReleaseTemplateAttr(server);
                } catch (Exception e) {
                    reporter.report(LogLevel.ERROR,"Failed to insert release template attr json to system");
                    reporter.report(LogLevel.ERROR, ExceptionUtils.getStackTrace(e));;
                }
            }
        });



    }

    private static void insertReleaseApprovalPlan(Server server) throws IOException {
        //check if already exists
        EntityReaderAPI entityReaderAPI = server.getEntityReaderAPI();
        List<String> layout = new ArrayList<String>();
        layout.add("Id");
        layout.add("DisplayLabel");
        List<EntityInstance> result = entityReaderAPI.readEntities("FulfillmentPlan", layout, new FilterBuilder(new SimpleFilterElement("DisplayLabel", "=", "Release – Approve Deployment")));
        if(result.size() == 1){
            reporter.report(LogLevel.INFO, "Release approval plan already exists, no need to insert again.");
            return;
        }

        //insert release approval plan
        EntityWriterAPI entityWriterAPI = server.getEntityWriterAPI();
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("sample/Release_Approval.json");
        String releaseApprovalPlanString = IOUtils.toString(inputStream, "UTF-8");
        JSONObject releaseApprovalJson = new JSONObject(releaseApprovalPlanString);
        Map<String, Object> fields = new HashMap<String, Object>();
        Set<String> set = releaseApprovalJson.keySet();
        for (String key : set) {
            fields.put(key, releaseApprovalJson.get(key));
        }
        EntityInstance entityInstance = new EntityInstance("FulfillmentPlan", fields, null);
        EntityBulkResult insertResult = entityWriterAPI.insertEntity(entityInstance);
        if(!insertResult.isFailed()){
            reporter.report(LogLevel.INFO, "Succeeded to insert release approval plan to system");
        } else {
            reporter.report(LogLevel.ERROR, "Failed to insert release approval plan to system");
        }
    }

    private static void insertReleaseTemplateAttr(Server server) throws IOException {
        String ENTITY_TEMPLATE_APPLICABLE_ATTRIBUTES = "EntityTemplateApplicableAttributes";

        //check if already exists
        RMSReaderAPI rmsReaderAPI = server.getRmsReaderAPI();
        List<RMSInstance> result = rmsReaderAPI.readResources(ENTITY_TEMPLATE_APPLICABLE_ATTRIBUTES, new FilterBuilder(new SimpleFilterElement("EntityType", "=", "Release")));
        if(result.size()  == 1) {
            reporter.report(LogLevel.INFO, "Release template attr json already exists, no need to insert again.");
            return;
        }

        //insert release template attr json
        RMSWriterAPI rmsWriterAPI = server.getRmsWriterAPI();
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("sample/release-template-attr.json");
        String releaseTemplateAttrString = IOUtils.toString(inputStream, "UTF-8");
        JSONObject releaseApprovalJson = new JSONObject(releaseTemplateAttrString);
        RMSInstance rmsInstance = new RMSInstance(ENTITY_TEMPLATE_APPLICABLE_ATTRIBUTES, releaseApprovalJson);
        rmsWriterAPI.insertResource(rmsInstance);

        List<RMSInstance> insertResult = rmsReaderAPI.readResources(ENTITY_TEMPLATE_APPLICABLE_ATTRIBUTES, new FilterBuilder(new SimpleFilterElement("EntityType", "=", "Release")));
        if(insertResult.size()  == 1) {
            reporter.report(LogLevel.INFO, "Succeeded to insert release template attr json to system");
        } else {
            reporter.report(LogLevel.ERROR, "Failed to insert release template attr json to system");
        }
    }

}




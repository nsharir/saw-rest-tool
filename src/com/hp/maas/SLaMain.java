package com.hp.maas;

import com.hp.maas.apis.Server;
import com.hp.maas.tools.FSOutput.FileSystemOutput;
import com.hp.maas.usecases.SLA.FileSystemReporter;
import com.hp.maas.usecases.SLA.FindDefaultSLAs;
import com.hp.maas.usecases.SLA.FindManyServices;
import com.hp.maas.usecases.SLA.FindWorkflowsWithSLA;
import com.hp.maas.utils.executers.multiTenant.MultiTenantExecutor;
import com.hp.maas.utils.executers.multiTenant.TenantFilterAllTenants;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 9:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class SLaMain {


    public static final String PROD_OPERATOR_PASSWORD = "----------";



    public static void main(String[] args) throws Exception{
        FileSystemReporter reporter;
        MultiTenantExecutor exe;


        FileSystemOutput out = new FileSystemOutput("C:\\temp\\SLA");

        reporter = new FileSystemReporter(out,"multi_service_austin");
        exe = new MultiTenantExecutor(astOper(),new TenantFilterAllTenants(), reporter);
        exe.run(new FindManyServices());

        reporter = new FileSystemReporter(out,"multi_service_london");
        exe = new MultiTenantExecutor(londonOper(),new TenantFilterAllTenants(), reporter);
        exe.run(new FindManyServices());


        reporter = new FileSystemReporter(out,"multi_defaults_austin");
        exe = new MultiTenantExecutor(astOper(),new TenantFilterAllTenants(), reporter);
        exe.run(new FindDefaultSLAs());

        reporter = new FileSystemReporter(out,"multi_defaults_london");
        exe = new MultiTenantExecutor(londonOper(),new TenantFilterAllTenants(), reporter);
        exe.run(new FindDefaultSLAs());

        reporter = new FileSystemReporter(out,"sla_workflows_london");
        exe = new MultiTenantExecutor(londonOper(),new TenantFilterAllTenants(), reporter);
        exe.run(new FindWorkflowsWithSLA(out));

        reporter = new FileSystemReporter(out,"sla_workflows_austin");
        exe = new MultiTenantExecutor( astOper(),new TenantFilterAllTenants(), reporter);
        exe.run(new FindWorkflowsWithSLA(out));



        reporter = new FileSystemReporter(out,"sla_workflows_austin");
        exe = new MultiTenantExecutor( astOper(),new TenantFilterAllTenants(), reporter);
        exe.run(new FindWorkflowsWithSLA(out));





    }



    private static Server londonOper(){
        Server londonOperator = new Server("https://mslon001pngx.saas.hp.com/", "saas.integration.user@hp.com", PROD_OPERATOR_PASSWORD, "166429523");
        londonOperator.authenticate();
        return londonOperator;
    }
    private static Server london(String tenantId){
        Server fork = londonOper().fork(tenantId);
        fork.authenticate();
        return fork;
    }

    private static Server astOper(){
        Server astOperator = new Server("https://msast001pngx.saas.hp.com/","saas.integration.user@hp.com",PROD_OPERATOR_PASSWORD,"270521405");
        astOperator.authenticate();
        return astOperator;
    }
    private static Server ast(String tenantId){
        Server fork = astOper().fork(tenantId);
        fork.authenticate();
        return fork;
    }

}



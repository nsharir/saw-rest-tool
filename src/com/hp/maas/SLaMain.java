package com.hp.maas;

import com.hp.maas.apis.Server;
import com.hp.maas.usecases.SLA.FindDefaultSLAs;
import com.hp.maas.usecases.SLA.FindManyServices;
import com.hp.maas.usecases.SLA.SLADefaultsReporter;
import com.hp.maas.usecases.data.DataMultiplier;
import com.hp.maas.utils.executers.multiTenant.MultiTenantExecutor;
import com.hp.maas.utils.executers.multiTenant.SingleTenantExecutor;
import com.hp.maas.utils.executers.multiTenant.TenantFilterAllTenants;
import com.hp.maas.utils.executers.reporters.ConsoleReporter;
import com.hp.maas.utils.executers.reporters.LogLevel;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 9:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class SLaMain {


    public static final String PROD_OPERATOR_PASSWORD = "*********************";



    public static void main(String[] args) throws Exception{
        SLADefaultsReporter reporter = new SLADefaultsReporter("C:\\temp\\SLA","defaultSla_ast");
        MultiTenantExecutor exe = new MultiTenantExecutor(astOper(),new TenantFilterAllTenants(), reporter);
        exe.run(new FindDefaultSLAs());

        SLADefaultsReporter reporter_multi = new SLADefaultsReporter("C:\\temp\\SLA","multi_service_austin");
        MultiTenantExecutor exeMulti = new MultiTenantExecutor(astOper(),new TenantFilterAllTenants(), reporter_multi);
        exeMulti.run(new FindManyServices());

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



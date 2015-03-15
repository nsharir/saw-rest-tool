package com.hp.maas;

import com.hp.maas.apis.Server;
import com.hp.maas.tools.FSOutput.FileSystemOutput;
import com.hp.maas.usecases.LocationEnumReferencesInMDCmd;
import com.hp.maas.usecases.SLA.FileSystemReporter;
import com.hp.maas.utils.executers.multiTenant.MultiTenantExecutor;
import com.hp.maas.utils.executers.multiTenant.TenantFilterAllTenants;
import com.hp.maas.utils.executers.reporters.LogLevel;
import com.hp.maas.utils.executers.reporters.Reporter;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 9:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {


    public static final String PROD_OPERATOR_PASSWORD = "-------";




    public static void main(String[] args) throws Exception{



    }



    private static Server stagingOper(){
        Server stagingOperator = new Server("https://msalb003sngx.saas.hp.com/", "saas.integration.user@hp.com", PROD_OPERATOR_PASSWORD, "451202925");
        stagingOperator.authenticate();
        return stagingOperator;
    }

    private static Server staging(String tenant){
        return stagingOper().fork(tenant);
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

    private static Server ast2Oper(){
        Server ast2Operator = new Server("https://msast002pngx.saas.hp.com/","saas.integration.user@hp.com",PROD_OPERATOR_PASSWORD,"583550288");
        ast2Operator.authenticate();
        return ast2Operator;
    }
    private static Server ast2(String tenantId){
        Server fork = ast2Oper().fork(tenantId);
        fork.authenticate();
        return fork;
    }

}



package com.hp.maas;

import com.hp.maas.apis.Server;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 9:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {


    public static final String PROD_OPERATOR_PASSWORD = "xxxxxxxxxxxxxxxxx";

    public static void main(String[] args) throws Exception{

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



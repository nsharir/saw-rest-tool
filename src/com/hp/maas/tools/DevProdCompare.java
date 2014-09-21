package com.hp.maas.tools;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.entity.EntityInstance;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/16/14
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class DevProdCompare {

    Server dev ;
    Server prod;

    public DevProdCompare(Server dev, Server prod) {
        this.dev = dev;
        this.prod = prod;
        init();
    }

    public void init(){
        dev.authenticate();
        prod.authenticate();
    }



    public void compareDevToProd(String type, String upnField){
        List<String> upn = new ArrayList<String>();
        upn.add(upnField);
        compareDevToProd(type, upn);

    }
    public void compareDevToProd(String type, List<String> upnFields){
        System.out.println("Comparing "+type+"...");

        DevProdCompareResult result = runComparison(type, upnFields);

        if(result == null){
            return;
        }

        if (result.getExistInDevNotInProd().size() > 0){
            System.out.println(type + " records needs to be pushed to production:");
            System.out.println("----------------------------------------------------");
            for (EntityInstance instance : result.getExistInDevNotInProd()) {
                System.out.println(type+"#"+instance.getFieldValue("Id")+": "+extractUpnValue(instance,upnFields));
            }
            System.out.println("----------------------------------------------------\n");

        }


        if (result.getExistInProdNotInDev().size() > 0){
            System.out.println(type + " records needs to be deleted from production:");
            System.out.println("----------------------------------------------------");
            for (EntityInstance instance : result.getExistInProdNotInDev()) {
                System.out.println(type+"#"+instance.getFieldValue("Id")+": "+extractUpnValue(instance,upnFields));
            }
            System.out.println("----------------------------------------------------\n");

        }

    }

    private DevProdCompareResult runComparison(String type, List<String> upnFields) {

        List<String> list = new ArrayList<String>();
        list.add("Id");
        list.addAll(upnFields);

        List<EntityInstance> devRecords = dev.getEntityReaderAPI().readEntities(type, list);
        List<EntityInstance> prodRecords = prod.getEntityReaderAPI().readEntities(type, list);

        List<String> errors = new ArrayList<String>();

        Map<Object,EntityInstance> devMap = new HashMap<Object, EntityInstance>();
        Map<Object,EntityInstance> prodMap = new HashMap<Object, EntityInstance>();

        for (EntityInstance devRecord : devRecords) {
            Object fieldValue = extractUpnValue(devRecord, upnFields);
            if (fieldValue == null){
                errors.add("Dev Tenant: "+type+ " record #"+devRecord.getFieldValue("Id")+" doesn't have a "+upnFields+".");
                continue;
            }
            EntityInstance oldEntity = devMap.put(fieldValue, devRecord);
            if (oldEntity != null){
                errors.add("Dev Tenant: " + type + " record #" + devRecord.getFieldValue("Id") + " have the same " + upnFields + " as record #" + oldEntity.getFieldValue("Id") + ". The duplicate value is [" + fieldValue + "]");
            }
        }

        for (EntityInstance prodRecord : prodRecords) {
            Object fieldValue = extractUpnValue(prodRecord, upnFields);
            if (fieldValue == null){
                errors.add("Prod Tenant: "+type+ " record #"+prodRecord.getFieldValue("Id")+" doesn't have a "+upnFields+".");
                continue;
            }
            EntityInstance oldEntity = prodMap.put(fieldValue,prodRecord );
            if (oldEntity != null){
                errors.add("Prod Tenant: " + type + " record #" + prodRecord.getFieldValue("Id") + " have the same " + upnFields + " as record #" + oldEntity.getFieldValue("Id") + ". The duplicate value is [" + fieldValue + "]");
            }
        }


        if (!errors.isEmpty()){
            System.out.println("\n["+type+"] There are some inconsistencies in your data:");
            System.out.println("---------------------------------------------------------------------------------------------");
            for (String error : errors) {
                System.out.println(error);
            }
            System.out.println("---------------------------------------------------------------------------------------------");
            return null;
        }

        List<EntityInstance> existInDevNotInProd = new ArrayList<EntityInstance>();
        List<EntityInstance> existInProdNotInDev = new ArrayList<EntityInstance>();

        for (EntityInstance devRecord : devRecords) {
            Object fieldValue = extractUpnValue(devRecord, upnFields);
            EntityInstance prodRecord = prodMap.get(fieldValue);
            if (prodRecord == null){
                existInDevNotInProd.add(devRecord);
            }
        }

        for (EntityInstance prodRecord : prodRecords) {
            Object fieldValue = extractUpnValue(prodRecord,upnFields);
            EntityInstance devRecord = devMap.get(fieldValue);
            if (devRecord == null){
                existInProdNotInDev.add(prodRecord);
            }
        }

        return new DevProdCompareResult(existInDevNotInProd, existInProdNotInDev);
    }

    private Object extractUpnValue(EntityInstance instance , List<String> upnFields){
        String str = null;
        for (String upnField : upnFields) {
            Object fieldValue = instance.getFieldValue(upnField);
            if (fieldValue != null){
                if (str != null){
                    str = str + "/"+fieldValue;
                }else{
                    str = ""+fieldValue;
                }
            }
        }
        return str;
    }

    //Server server = new Server("https://mslon001pngx.saas.hp.com/","nadav.sharir@hp.com","************","733658382"); //DEV Aer Lingus
    //Server server = new Server("https://msast001pngx.saas.hp.com/","lital.alon@hp.com","Password1","751757050"); //Production tenant QA - Austin
    //Server server = new Server("https://msalb003sngx.saas.hp.com/","lital.alon@hp.com","Password3","191971052"); //staging
}

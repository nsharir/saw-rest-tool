package com.hp.maas.tools.customers;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.entity.EntityInstance;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.apis.model.query.SimpleFilterElement;
import com.hp.maas.tools.DevProdCompare;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/16/14
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class AerLingus {

    private Server ALDev =  new Server("https://mslon001pngx.saas.hp.com/","nadav.sharir@hp.com","************","733658382"); //London - one of our trail tenants
    private Server ALProd = new Server("https://mslon001pngx.saas.hp.com/","nadav.sharir@hp.com","************","226830052"); //London - R&D tenant

    public AerLingus() {
        ALDev.authenticate();
        ALProd.authenticate();
    }

    public void runDevToProdCompare(){
        DevProdCompare devProdCompare = new DevProdCompare(ALDev, ALProd);


        devProdCompare.compareDevToProd("Location", "Name");
        devProdCompare.compareDevToProd("Company", "DisplayLabel");
        devProdCompare.compareDevToProd("ITProcessRecordCategory", Arrays.asList("Level1Parent","Level2Parent","DisplayLabel"));
        devProdCompare.compareDevToProd("Category", "DisplayLabel");
        devProdCompare.compareDevToProd("ServiceDefinition", "DisplayLabel");
        devProdCompare.compareDevToProd("ActualService","DisplayLabel");
        //devProdCompare.compareDevToProd("PersonGroup","Upn");

    }

    public void deleteEntityFromDev(String type, Object id) {
        List<EntityInstance> instances = ALDev.getEntityReaderAPI().readEntities(type, Arrays.asList("Id"), new FilterBuilder(new SimpleFilterElement("Id", "=", id)));
        ALDev.getEntityWriterAPI().deleteEntities(instances);
    }
}

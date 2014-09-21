package com.hp.maas.apis.model.devToProd.sync;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.entity.EntityInstance;
import com.hp.maas.apis.model.query.APISyntaxFilterElement;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.configuration.Configuration;
import com.hp.maas.configuration.IdentificationRules;
import com.hp.maas.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/15/14
 * Time: 12:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class UpnBasedQuery {
    private Server server;

    public UpnBasedQuery(Server server) {
        this.server = server;
    }

    public Map<Object, EntityInstance> getUPNMap(String type, List<EntityInstance> similarRecords){
        Map<Object,EntityInstance> upns = new HashMap<Object, EntityInstance>();

        IdentificationRules identificationRules = Configuration.getIdentificationRules();
        String upnField = identificationRules.getIdentityField(type);
        for (EntityInstance record : similarRecords) {
            Object fieldValue = record.getFieldValue(upnField);
            if (fieldValue == null){
                throw new RuntimeException("Record of type "+type+" and Id "+record.getFieldValue("Id")+" Do not have a value in identity field named "+upnField);
            }
            upns.put(fieldValue,record);
        }

        String ids = StringUtils.toCommaSeparatedString(new ArrayList<Object>(upns.keySet()),true);

        List<EntityInstance> instances = server.getEntityReaderAPI().readFullLayout(type, new FilterBuilder(new APISyntaxFilterElement("EmsCreationTime btw (0,2335199999) and "+upnField + " in (" + ids + ")")));

        Map<Object,EntityInstance> map = new HashMap<Object, EntityInstance>();

        for (EntityInstance instance : instances) {
            Object fieldValue = instance.getFieldValue(upnField);
            if (fieldValue == null){
                throw new RuntimeException("Record of type "+type+" and Id "+instance.getFieldValue("Id")+" Do not have a value in identity field named "+upnField);
            }
            map.put(fieldValue,instance);
        }

        return map;
    }
}

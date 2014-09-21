package com.hp.maas.apis.model.devToProd.sync;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.entity.EntityBulkResult;
import com.hp.maas.apis.model.entity.EntityInstance;
import com.hp.maas.apis.model.entity.EntityUtils;
import com.hp.maas.apis.model.metadata.EntityTypeDescriptor;
import com.hp.maas.apis.model.metadata.FieldDescriptor;
import com.hp.maas.configuration.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/14/14
 * Time: 10:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class SyncStepInsertWithInternalLoops implements SyncStep {

    private String type;
    private List<EntityInstance> originalEntitiesToInsert;
    private String stepInfo;
    private Server targetServer;


    public SyncStepInsertWithInternalLoops(String type,List<EntityInstance> originalEntitiesToInsert, String stepInfo, Server targetServer) {
        this.type = type;
        this.originalEntitiesToInsert = originalEntitiesToInsert;
        this.stepInfo = stepInfo;
        this.targetServer = targetServer;
    }

    private void doIt(List<EntityInstance> originalInsertList) {

        List<LoopEntityWrapper> entites = new ArrayList<LoopEntityWrapper>();

        List<String> loopFields = new ArrayList<String>();
        EntityTypeDescriptor md = targetServer.getMetadataAPI().getEntityDescriptor(type);
        for (FieldDescriptor descriptor : md.getFields()) {
            if (descriptor.hasReference() && descriptor.getReference().getTargetType().equals(type)){
                loopFields.add(descriptor.getName());
            }
        }

        String identityField = Configuration.getIdentificationRules().getIdentityField(type);

        Map<Object,Object> originalIdToUpnMap = new HashMap<Object, Object>();

        for (EntityInstance instance : originalInsertList) {

            Object upnValue = instance.getFieldValue(identityField);
            if (upnValue == null){
                throw new RuntimeException("Record of type "+type+" and Id "+instance.getFieldValue("Id")+" Do not have a value in identity field named "+identityField);
            }
            originalIdToUpnMap.put(instance.getFieldValue("Id"),upnValue);


            EntityInstance cloned = new EntityInstance(instance);

            for (String loopField : loopFields) {
                cloned.removeField(loopField);
            }

            cloned.removeField("Id");
            cloned.removeField("LastUpdateTime");

            entites.add(new LoopEntityWrapper(instance, cloned));
        }


        List<EntityInstance> insertList = new ArrayList<EntityInstance>();
        for (LoopEntityWrapper entity : entites) {
            insertList.add(entity.clone);
        }
        EntityBulkResult entityBulkResult = targetServer.getEntityWriterAPI().insertEntities(insertList);

        UpnBasedQuery upnBasedQuery = new UpnBasedQuery(targetServer);

        Map<Object, EntityInstance> upnMap = upnBasedQuery.getUPNMap(type, originalInsertList);

        for (LoopEntityWrapper entity : entites) {
            for (String loopField : loopFields) {
                Object oldId = entity.original.getFieldValue(loopField);
                if (oldId != null){
                    Object upnValue = originalIdToUpnMap.get(oldId);
                    Object newId = upnMap.get(upnValue).getFieldValue("Id");
                    entity.clone.setFieldValue(loopField, newId);
                }
            }
        }



        List<EntityInstance> updateList = new ArrayList<EntityInstance>();
        for (LoopEntityWrapper entity : entites) {
            updateList.add(entity.clone);
        }

        EntityUtils.merge(updateList,upnMap, identityField);

        targetServer.getEntityWriterAPI().updateEntities(updateList);

    }



    @Override
    public String describe() {
        return "Create "+originalEntitiesToInsert.size()+ " records - "+stepInfo;
    }

    @Override
    public String detailedLog() {
        StringBuilder str = new StringBuilder();
        str.append(describe()).append("\n");
        str.append("--------------------------------------");

        for (EntityInstance instance : originalEntitiesToInsert) {
            str.append(instance);
        }
        str.append("--------------------------------------");
        return str.toString();
    }

    @Override
    public void execute() {
        doIt(originalEntitiesToInsert);
    }
}

package com.hp.maas.usecases.data;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.entity.EntityBulkResult;
import com.hp.maas.apis.model.entity.EntityInstance;
import com.hp.maas.apis.model.metadata.EntityTypeDescriptor;
import com.hp.maas.apis.model.metadata.FieldDescriptor;
import com.hp.maas.apis.model.query.APISyntaxFilterElement;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.configuration.Configuration;
import com.hp.maas.utils.executers.multiTenant.TenantCommand;
import com.hp.maas.utils.executers.reporters.LogLevel;
import com.hp.maas.utils.executers.reporters.Reporter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sharir on 26/01/2015.
 */
public class DataMultiplier implements TenantCommand {

    public static final int BUCKET_SIZE = 50;
    private String entityType;
    private int minId = 0;
    private int maxId;
    private String postfix;
    private Set<String> textAttributes;


    public DataMultiplier(String entityType, int minId, int maxId, String postfix, String ... textAttributes) {
        this.entityType = entityType;
        this.minId = minId;
        this.maxId = maxId;
        this.postfix = postfix;
        this.textAttributes = new HashSet<String>();
        if (textAttributes == null || textAttributes.length == 0){
            throw new RuntimeException("You must provide at least one text attribute");
        }
        for (String textAttribute : textAttributes) {
            this.textAttributes.add(textAttribute);
        }
    }


    @Override
    public void execute(Server server, Tenant tenant, Reporter reporter) {

        List<EntityInstance> instances = server.getEntityReaderAPI().readFullLayout(entityType, new FilterBuilder(new APISyntaxFilterElement("Id >="+minId+" and Id <="+maxId)));

        reporter.report(LogLevel.INFO, "About to clone "+instances.size() +" "+entityType+" entities.");

        EntityTypeDescriptor md = server.getMetadataAPI().getEntityDescriptor(entityType);


        for (EntityInstance instance : instances) {
            instance.removeField("Id");
            for (String f : textAttributes) {
                if (instance.hasField(f)){
                    Object value = instance.getFieldValue(f);
                    instance.setFieldValue(f,value+postfix);
                }
            }
        }

        int buckets = instances.size() / BUCKET_SIZE;

        if (buckets % BUCKET_SIZE != 0){
            buckets++;
        }

       for (int i=0; i<buckets ; i++){
           int fromIndex = i * BUCKET_SIZE;
           int toIndex = fromIndex + BUCKET_SIZE;
           toIndex = Math.min(toIndex, instances.size());
           EntityBulkResult result = server.getEntityWriterAPI().insertEntities(instances.subList(fromIndex, toIndex));
           reporter.report(LogLevel.INFO, "Clone is done. [succeeded = "+result.getSucceeded().size()+"] "+" [failures = "+result.getFailures().size()+"]");
       }



    }
}

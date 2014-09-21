package com.hp.maas.apis.model.devToProd;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.devToProd.sync.SyncStepInsertWithInternalLoops;
import com.hp.maas.apis.model.entity.EntityInstance;
import com.hp.maas.apis.model.entity.EntityInstanceParser;
import com.hp.maas.apis.model.metadata.EntityTypeDescriptor;
import com.hp.maas.apis.model.metadata.FieldDescriptor;
import com.hp.maas.apis.model.query.APISyntaxFilterElement;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.configuration.Configuration;
import com.hp.maas.utils.Log;
import com.hp.maas.utils.StringUtils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/13/14
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class SourceToTargetManager {
    public static final String OFFERING = "Offering";
    private Server source ;
    private Server target ;



    private Map<String,List<EntityInstance>> sourceData = new HashMap<String, List<EntityInstance>>();

    SourceReferences sourceUnresolvedReferences = new SourceReferences();
    private Map<String,Map<String,EntityInstance>> sourceResolvedIds = new  HashMap<String,Map<String,EntityInstance>>();
    private Map<String,Set<String>> sourceTypeDependency = new HashMap<String, Set<String>>();



    public SourceToTargetManager(Server source, Server target) {
        this.source = source;
        this.target = target;
    }

    public void fullFlow(boolean doActualModifications){
        //clear();
        source.authenticate();
        if (target != null){
            target.authenticate();
        }
        loadDevData();

        calculateDevAdditionalRequiredRelations();

        resolvedSourceIDs();

        printItemsToResolve();

        if (doActualModifications){
            runSync();
        }
        //buildMappingTables();

    }

    private void runSync() {
        List<EntityInstance> instances = sourceData.get("Location");
        SyncStepInsertWithInternalLoops step = new SyncStepInsertWithInternalLoops("Location",instances, "Location sync (2 steps) ",target);
        step.execute();
    }

    private void resolvedSourceIDs() {

        //for (Map.Entry<String, LinkedHashSet<String>> entry : sourceIdsToResolve.entrySet()) {
        for (String type : sourceUnresolvedReferences.getTypes()){
            String identityField = Configuration.getIdentificationRules().getIdentityField(type);

            if (identityField == null){
                throw new RuntimeException("Identity field must be defined for "+type);
            }

            List<String> layout = new ArrayList<String>();
            layout.add("Id");
            layout.add(identityField);

            ArrayList idsList = new ArrayList<String>(sourceUnresolvedReferences.getIds(type));
            String ids = StringUtils.toCommaSeparatedString(idsList,false);
            FilterBuilder filter = new FilterBuilder(new APISyntaxFilterElement("Id in ("+ids+")"));

            List<EntityInstance> instances = source.getEntityReaderAPI().readEntities(type, layout, filter);

            Map<String, EntityInstance> idsToInstance = new HashMap<String, EntityInstance>();

            for (EntityInstance instance : instances) {
                idsToInstance.put((String) instance.getFieldValue("Id"),instance);
            }

            if (instances.size() != idsList.size()){
                Log.error("Broken links found!!");
                for (Object id : idsList) {
                    EntityInstance instanceById = idsToInstance.get(id);
                    if (instanceById == null){
                        Log.error("Broken link to "+type+" of id "+id+":");
                        Collection<ReferenceSource> sources = sourceUnresolvedReferences.getSources(type, id);
                        for (ReferenceSource referenceSource : sources) {
                            Log.error("   Broken link from source "+ referenceSource+ " to "+type+" of id "+id);
                        }

                    }
                }
                throw new RuntimeException("There are broken links that refers to none exist entity of type "+type);
            }



            sourceResolvedIds.put(type,idsToInstance);
        }
    }

    private void printItemsToResolve() {
        for (Map.Entry<String, Map<String,EntityInstance>> entry : sourceResolvedIds.entrySet()) {
            System.out.println("\n\nEntity Type: "+entry.getKey());
            System.out.println("---------------------------------------");
            for (EntityInstance instance : entry.getValue().values()) {
                System.out.println(instance);
            }
            System.out.println("---------------------------------------");
        }

        System.out.println("Dependencies:");
        for (Map.Entry<String, Set<String>> entry : sourceTypeDependency.entrySet()) {
            Set<String> targets = entry.getValue();
            for (String target : targets) {
                System.out.println(entry.getKey() +" ---> "+target);
            }

        }
    }

    private void calculateDevAdditionalRequiredRelations() {
        if (!Configuration.getTypesToPull().contains(OFFERING)){
            return;
        }
        List<EntityInstance> offerings = sourceData.get(OFFERING);
        if (offerings == null){
            return;
        }


        for (EntityInstance offering : offerings) {
            Object templateValues = offering.getFieldValue("TemplateValues");
            if (templateValues == null){
                continue;
            }
            EntityInstance defaults = EntityInstanceParser.parseRequestDefaultValuesComplexType((String) templateValues);
            if (defaults == null){
                continue;
            }
            Collection<String> fieldsNames = defaults.getFieldsNames();
            EntityTypeDescriptor requestMD = source.getMetadataAPI().getEntityDescriptor("Request");
            for (String fieldsName : fieldsNames) {
                FieldDescriptor requestField = requestMD.getField(fieldsName);
                if (requestField.hasReference()){
                    sourceUnresolvedReferences.addReference(OFFERING,(String)offering.getFieldValue("Id"),requestField.getReference().getTargetType(),(String)defaults.getFieldValue(fieldsName),"TemplateValues");
                    Set<String> targets = sourceTypeDependency.get(OFFERING);
                    if (targets == null){
                        targets = new HashSet<String>();
                        sourceTypeDependency.put(OFFERING,targets);
                    }
                    targets.add(requestField.getReference().getTargetType());
                }
            }
        }


    }

    private void loadDevData() {
        List<String> typesToPull = Configuration.getTypesToPull();
        for (String type : typesToPull) {

            EntityTypeDescriptor typeMD = source.getMetadataAPI().getEntityDescriptor(type);

            Log.log("About to read all entities of: "+type+"...");

            List<EntityInstance> instances = source.getEntityReaderAPI().readFullLayout(type);

            Log.log(instances.size()+" "+type +" where read.\n");

            sourceData.put(type, instances);

            List<FieldDescriptor> realations = new ArrayList<FieldDescriptor>();
            for (FieldDescriptor fieldDescriptor : typeMD.getFields()) {
                if (fieldDescriptor.hasReference()){
                    realations.add(fieldDescriptor);
                }
            }
            for (EntityInstance instance : instances) {
                for (FieldDescriptor relation : realations) {
                    Object refValue = instance.getFieldValue(relation.getName());
                    if (refValue != null){
                        sourceUnresolvedReferences.addReference(type, (String) instance.getFieldValue("Id"), relation.getReference().getTargetType(), (String) refValue, relation.getName());
                        Set<String> targets = sourceTypeDependency.get(type);
                        if (targets == null){
                            targets = new HashSet<String>();
                            sourceTypeDependency.put(type,targets);
                        }
                        targets.add(relation.getReference().getTargetType());
                    }
                }
            }
        }
    }


}

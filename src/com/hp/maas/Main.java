package com.hp.maas;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.entity.EntityBulkResult;
import com.hp.maas.apis.model.entity.EntityInstance;
import com.hp.maas.apis.model.metadata.EntityTypeDescriptor;
import com.hp.maas.apis.model.metadata.FieldDescriptor;
import com.hp.maas.apis.model.query.APISyntaxFilterElement;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.apis.model.query.IdsFilterElement;
import com.hp.maas.apis.model.query.SimpleFilterElement;
import com.hp.maas.configuration.Configuration;
import com.hp.maas.tools.cutomers.AerLingus;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 9:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    //public static final String TYPE = "Request";

    //
    //
    //


    public static void main(String[] args) {

        Configuration.load();


        Server ALProd = new Server("https://mslon001pngx.saas.hp.com/","nadav.sharir@hp.com","************","226830052");
        Server ALDev =  new Server("https://mslon001pngx.saas.hp.com/","nadav.sharir@hp.com","************","733658382");

        ALDev.authenticate();
        ALProd.authenticate();

        List<EntityInstance> listDev = ALDev.getEntityReaderAPI().readFullLayout("PersonGroup", new FilterBuilder(new SimpleFilterElement("Upn", "=", "10989")));    //
        List<EntityInstance> listProd = ALProd.getEntityReaderAPI().readFullLayout("PersonGroup", new FilterBuilder(new SimpleFilterElement("Upn", "=", "10810")));    //

        System.out.println(listDev.size());
        System.out.println(listProd.size());

        Object devId = listDev.get(0).getFieldValue("Id");
        System.out.println("Dev ID:" + devId);

        Object prodId = listProd.get(0).getFieldValue("Id");
        System.out.println("Prod ID:" + prodId);


         listDev = ALDev.getEntityReaderAPI().readFullLayout("PersonGroup", new FilterBuilder(new SimpleFilterElement("Id", "=", prodId)));    //
         listProd = ALProd.getEntityReaderAPI().readFullLayout("PersonGroup", new FilterBuilder(new SimpleFilterElement("Id", "=", devId)));    //

        System.out.println(listDev.size());
        System.out.println(listProd.size());

        //listDev.get(0).getFieldValue("Name");




        //findBrokenLocationsFromPerson();

        //AerLingus AL = new AerLingus();
        //AL.runDevToProdCompare();


    }


        //AL.deleteEntityFromDev("Location",11071);

        /*AerLingus AL = new AerLingus();
        AL.runDevToProdCompare();
*/
       /* Server ALDev =  new Server("https://mslon001pngx.saas.hp.com/","nadav.sharir@hp.com","************","733658382"); //London - one of our trail tenants
        Server ALProd = new Server("https://mslon001pngx.saas.hp.com/","nadav.sharir@hp.com","************","226830052");

        ALDev.authenticate();
        ALProd.authenticate();

        EntityTypeDescriptor dev = ALDev.getMetadataAPI().getEntityDescriptor("Person");
        EntityTypeDescriptor prod = ALProd.getMetadataAPI().getEntityDescriptor("Person");

        List<FieldDescriptor> fields = dev.getFields();
        for (FieldDescriptor field : fields) {
            if (prod.getField(field.getName()) == null){
                System.out.println(field.getName());
            }
        }*/


        /*
        EntityTypeDescriptor md = ALDev.getMetadataAPI().getEntityDescriptor("Person");

        List<FieldDescriptor> fields = md.getFields();

        Map<String,FieldDescriptor> refs = new HashMap<String,FieldDescriptor>();

        for (FieldDescriptor field : fields) {
            if (field.hasReference()){
                refs.put(field.getName(), field);
            }
        }


        Set<String> hasLinks = new HashSet<String>();

        for (EntityInstance person : allDevPersons) {
            for (String ref : refs.keySet()) {
                Object value = person.getFieldValue(ref);
                if (value != null){
                    hasLinks.add(ref);
                }
            }
        }

        for (String hasLink : hasLinks) {
            System.out.println(hasLink);
        }*/




        //https://mslon001pngx.saas.hp.com/rest/733658382/ems/Person?filter=(IsMaasUser+%3D+%27false%27+and+IsSystemIntegration+!%3D+%27true%27+and+IsSystem+!%3D+%27true%27)&layout=Id,Name,Email,HomeLocation,Upn,Locale,Avatar,Title&meta=totalCount&size=250

        //Server londonTrailTenantTrail = new Server("https://mslon001pngx.saas.hp.com/","nadav.sharir@hp.com","************","273954719"); //London - one of our trail tenants
        //Server londonRnDTenant = new Server("https://mslon001pngx.saas.hp.com/","nadav.sharir@hp.com","************","787406283"); //London - R&D tenant
        //Server server = new Server("https://mslon001pngx.saas.hp.com/","nadav.sharir@hp.com","************","733658382"); //DEV Aer Lingus
        //Server server = new Server("https://msast001pngx.saas.hp.com/","lital.alon@hp.com","Password1","75AERLingus1757050"); //Production tenant QA - Austin
        //Server server = new Server("https://msalb003sngx.saas.hp.com/","lital.alon@hp.com","Password3","191971052"); //staging


       /* Server ALProd = new Server("https://mslon001pngx.saas.hp.com/","nadav.sharir@hp.com","************","226830052"); //London - R&D tenant
        ALProd.authenticate();
        List<EntityInstance> entityInstances = ALProd.getEntityReaderAPI().readFullLayout("ITProcessRecordCategory", new FilterBuilder(new IdsFilterElement(10881,10891,10894)));
        System.out.println(entityInstances.size());
        ALProd.getEntityWriterAPI().deleteEntities(entityInstances);
*/
/* server.authenticate();
        List<EntityInstance> entityInstances = server.getEntityReaderAPI().readFullLayout("ITProcessRecordCategory", new FilterBuilder(new SimpleFilterElement("Id", "=", 11306)));
        EntityInstance entityInstance = entityInstances.get(0);
        entityInstance.setFieldValue("DisplayLabel","Reader");
        server.getEntityWriterAPI().updateEntity(entityInstance);*/


        //   server.authenticate();
        //server.getEntityReaderAPI().readFullLayout("Person");
       /* List<EntityInstance> entityInstances = server.getEntityReaderAPI().readEntities("Person", Arrays.asList("Id", "Upn"));
        System.out.println("Upn,Id");
        for (EntityInstance instance : entityInstances) {
            System.out.println(instance.getFieldValue("Upn")+","+instance.getFieldValue("Id"));
        }*/
        //cloneChangeModel(server);
/*


        SourceToTargetManager manager = new SourceToTargetManager(londonRnDTenant, londonTrailTenantTrail);
        manager.fullFlow(true);
*/

       /* server.authenticate();
        List<EntityInstance> location = server.getEntityReaderAPI().readFullLayout("Location", new FilterBuilder(new APISyntaxFilterElement(" Id in (11046,11047,11048,11101,11102)")));
        server.getEntityWriterAPI().deleteEntities(location);*/
/*
        londonTrailTenantTrail.authenticate();

        List<EntityInstance> instances = londonTrailTenantTrail.getEntityReaderAPI().readFullLayout("Location");


        londonTrailTenantTrail.getEntityWriterAPI().deleteEntities(instances);
*/

       /* List<EntityInstance> instances = server.getEntityReaderAPI().readFullLayout("Location", new FilterBuilder(new SimpleFilterElement("Id", "=", 10475)));
        server.getEntityWriterAPI().deleteEntity(instances.get(0));*/

        //server.authenticate();

        /*FilterBuilder filter = new FilterBuilder(new SimpleFilterElement("Active","=",true));
        filter.and(new APISyntaxFilterElement("AssignedToPerson in (10103)"));


        List<EntityInstance> entities = printEntitesOfType(server, "Request",filter);*/

        //printEntitesOfType(server, "Offering",null);

       /* List<String> layout = new ArrayList<String>();
        layout.add("TemplateValues");
        layout.add("Id");
        List<EntityInstance> entities = server.getEntityReaderAPI().readEntities("Offering",layout);
        for (EntityInstance entity : entities) {
            //System.out.println(entity);
            EntityInstance requestDefaults = EntityInstanceParser.parseRequestDefaultValuesComplexType((String) entity.getFieldValue("TemplateValues"));
            System.out.println(requestDefaults);
        }*/

        //printMD(server);


        //List<EntityInstance> entities = printEntitesOfType(server, "Person",null);

        //duplicateFirstEntity(server, entities);




    private static void findBrokenLocationsFromPerson() {
        Server ALProd = new Server("https://mslon001pngx.saas.hp.com/","nadav.sharir@hp.com","************","226830052"); //London - R&D tenant
        ALProd.authenticate();

        System.out.println("Reading prod contacts....");
        List<EntityInstance> persons_prod_1 = ALProd.getEntityReaderAPI().readFullLayout("Person", new FilterBuilder(new APISyntaxFilterElement(" Id btw (1,17000)")));
        List<EntityInstance> persons_prod_2 = ALProd.getEntityReaderAPI().readFullLayout("Person", new FilterBuilder(new APISyntaxFilterElement(" Id btw (17001,20000)")));
        List<EntityInstance> allProdPersons = new ArrayList<EntityInstance>();
        allProdPersons.addAll(persons_prod_1);
        allProdPersons.addAll(persons_prod_2);

        List<EntityInstance> locations = ALProd.getEntityReaderAPI().readFullLayout("Location");

        Map<Object,EntityInstance> loctionMap = new HashMap<Object, EntityInstance>();
        for (EntityInstance location : locations) {
            loctionMap.put(location.getFieldValue("Id"),location);
        }

        for (EntityInstance person : allProdPersons) {
            Object location = person.getFieldValue("Location");
            if (location == null){
                continue;
            }
            EntityInstance entityInstance = loctionMap.get(location);
            if (entityInstance == null){
                System.out.println("Broken link to location #"+location+" for user with Upn "+person.getFieldValue("Upn"));
            }else{
                System.out.println(person.getFieldValue("Upn")+"_"+entityInstance.getFieldValue("Name"));

            }
        }
    }

    private static  List<List<EntityInstance>> bulkSplitter (List<EntityInstance> entities){
        List<List<EntityInstance>> results = new ArrayList<List<EntityInstance>>();
        List<EntityInstance> current = new ArrayList<EntityInstance>();
        results.add(current);

        for (EntityInstance entity : entities) {
            current.add(entity);
            if (current.size() == 250){
                current = new ArrayList<EntityInstance>();
                results.add(current);
            }
        }

        return results;
    }

    private static void AerLingusContactsSync() {
        Server ALDev =  new Server("https://mslon001pngx.saas.hp.com/","nadav.sharir@hp.com","************","733658382"); //London - one of our trail tenants
        ALDev.authenticate();
        System.out.println("Reading dev contacts....");

        List<EntityInstance> persons_1 = ALDev.getEntityReaderAPI().readFullLayout("Person", new FilterBuilder(new APISyntaxFilterElement("IsMaasUser = false and Id btw (1,17500)")));
        List<EntityInstance> persons_2 = ALDev.getEntityReaderAPI().readFullLayout("Person", new FilterBuilder(new APISyntaxFilterElement("IsMaasUser = false and Id btw (17501,30000)")));
        List<EntityInstance> allDevPersons = new ArrayList<EntityInstance>();
        allDevPersons.addAll(persons_1);
        allDevPersons.addAll(persons_2);

        System.out.println("Dev has "+allDevPersons.size()+ " contacts");


        Server ALProd = new Server("https://mslon001pngx.saas.hp.com/","nadav.sharir@hp.com","************","226830052"); //London - R&D tenant
        ALProd.authenticate();

        System.out.println("Reading prod contacts....");
        List<EntityInstance> persons_prod_1 = ALProd.getEntityReaderAPI().readFullLayout("Person", new FilterBuilder(new APISyntaxFilterElement("IsMaasUser = false and Id btw (1,13500)")));
        List<EntityInstance> persons_prod_2 = ALProd.getEntityReaderAPI().readFullLayout("Person", new FilterBuilder(new APISyntaxFilterElement("IsMaasUser = false and Id btw (13500,30000)")));
        List<EntityInstance> allProdPersons = new ArrayList<EntityInstance>();
        allProdPersons.addAll(persons_prod_1);
        allProdPersons.addAll(persons_prod_2);


        Map<Object,EntityInstance> prodPersonMap = new HashMap<Object, EntityInstance>();
        for (EntityInstance person : allProdPersons) {
            prodPersonMap.put(person.getFieldValue("Upn"), person);
        }

        System.out.println("Prod has "+allProdPersons.size()+ " contacts");

        System.out.println("Loading locations from dev...");
        List<EntityInstance> allDevLocations = ALDev.getEntityReaderAPI().readEntities("Location", Arrays.asList("Name", "Id"));


        Map<Object,Object> devLocationMap = new HashMap<Object, Object>();

        for (EntityInstance location : allDevLocations) {
            devLocationMap.put(location.getFieldValue("Id"),location.getFieldValue("Name"));
        }

        System.out.println("Loading locations from prod...");
        List<EntityInstance> allProdLocations = ALProd.getEntityReaderAPI().readEntities("Location", Arrays.asList("Name", "Id"));
        Map<Object,Object> prodLocationMap = new HashMap<Object, Object>();

        for (EntityInstance location : allProdLocations) {
            prodLocationMap.put(location.getFieldValue("Name"),location.getFieldValue("Id"));
        }

        Set<EntityInstance> existingPersons = new HashSet<EntityInstance>();

        List<EntityInstance> personsToInsert = new ArrayList<EntityInstance>();

        for (EntityInstance devPerson : allDevPersons) {
            EntityInstance prodPerson = prodPersonMap.get(devPerson.getFieldValue("Upn"));
            if (prodPerson != null){
                existingPersons.add(prodPerson);
            }else{
                devPerson.removeField("Id");
                devPerson.removeField("LastUpdateTime");
                Object location = devPerson.getFieldValue("Location");

                if (location != null){
                    Object devLocationUpn = devLocationMap.get(location);
                    if (devLocationUpn == null){
                        throw new RuntimeException("Missing location with Id "+location+" in dev...: ");
                    }
                    Object prodLocationId = prodLocationMap.get(devLocationUpn);
                    if (prodLocationId == null){
                        throw new RuntimeException("Missing location in production: "+devLocationUpn);
                    }
                    devPerson.setFieldValue("Location",prodLocationId);
                }

                Object company = devPerson.getFieldValue("Company");
                if (company != null){
                    if (company.toString().equals("14045")){
                        devPerson.setFieldValue("Company",13575);
                    }
                    else if (company.toString().equals("14195")){
                        devPerson.setFieldValue("Company",13511);
                    }
                    else{
                        throw new RuntimeException();
                    }

                }

                if (company != null || location !=null){
                    personsToInsert.add(devPerson);
                }
            }

        }

        System.out.println("Number of existing contacts: "+ existingPersons.size());

        System.out.println("About to insert" +personsToInsert.size()+ " contacts:");

        List<List<EntityInstance>> lists = bulkSplitter(personsToInsert);

        for (int i=0;i<lists.size();i++){
            List<EntityInstance> bulk = lists.get(i);
            System.out.println("Syncing bulk " +new Integer(i+1)+ " from "+lists.size() );
            ALProd.getEntityWriterAPI().insertEntities(bulk);
        }

    }
       /*
       Object company = instance.getFieldValue("Company");
            if (company != null){
                if (company.toString().equals("14045")){
                    instance.setFieldValue("Company",13575);
                }
                else if (company.toString().equals("14195")){
                    instance.setFieldValue("Company",13511);
                }
                else{
                    throw new RuntimeException();
                }
        */

    private static void duplicateFirstEntity(Server server, List<EntityInstance> entities) {
        EntityInstance entityInstance = entities.get(0);
        System.out.println(server.getEntityWriterAPI().insertEntity(entityInstance));
    }

    private static List<EntityInstance> printEntitesOfType(Server server, String type,FilterBuilder filter) {
        List<EntityInstance> entities = server.getEntityReaderAPI().readFullLayout(type,filter);
        for (EntityInstance entity : entities) {
            System.out.println(entity);
        }
        return entities;
    }

    private static void printMD(Server server, String type) {
        EntityTypeDescriptor incidentMD = server.getMetadataAPI().getEntityDescriptor(type);
        System.out.println(incidentMD);
    }

    private static void findEntityRelations(Server server, EntityInstance entityInstance) {
        Collection<String> fieldsNames = entityInstance.getFieldsNames();
        EntityTypeDescriptor entityDescriptor = server.getMetadataAPI().getEntityDescriptor(entityInstance.getType());
        for (String name : fieldsNames) {
            FieldDescriptor field = entityDescriptor.getField(name);
            if (field.hasReference()){
                System.out.println(field.getReference());
            }
        }
    }

    private static void cloneChangeModel(Server server) {
        server.authenticate();

        List<EntityInstance> entities = server.getEntityReaderAPI().readFullLayout("EntityModel");

       /* for (EntityInstance entity : entities) {
            System.out.println(entity);
        }*/

        for (EntityInstance entity : entities) {
            entity.removeField("Id");
            entity.setFieldValue("DisplayLabel", entity.getFieldValue("DisplayLabel") + "_4");
            entity.setFieldValue("PhaseId","Draft");
        }

        EntityBulkResult response = server.getEntityWriterAPI().insertEntities(entities);

    }
}

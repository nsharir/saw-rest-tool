package com.hp.maas.apis;

import com.hp.maas.apis.model.entity.EntityInstance;
import com.hp.maas.apis.model.entity.EntityInstanceParser;
import com.hp.maas.apis.model.metadata.EntityReferenceDescriptor;
import com.hp.maas.apis.model.metadata.EntityTypeDescriptor;
import com.hp.maas.apis.model.metadata.FieldDescriptor;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.configuration.Configuration;
import com.hp.maas.configuration.IdentificationRules;
import com.hp.maas.utils.ConnectionUtils;
import com.hp.maas.utils.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class EntityReaderAPI {
    public static final String ENTITIES = "entities";
    public static final String META = "meta";
    public static final String COMPLETION_STATUS = "completion_status";
    public static final String STATUS_OK = "OK";
    private Server server;

     EntityReaderAPI(Server server) {
        this.server = server;
    }

    public List<EntityInstance> readEntities(String entityType,List<String> layout){
        return  readEntities(entityType, layout,null);
    }

    public List<EntityInstance> readEntities(String entityType,List<String> layout, FilterBuilder filter){
        List<String> enrichedLayout = new ArrayList<String>();
        enrichedLayout.addAll(layout);

        /*EntityTypeDescriptor md = server.getMetadataAPI().getEntityDescriptor(entityType);
        IdentificationRules identificationRules = Configuration.getIdentificationRules();

        for (String field : layout) {
            EntityReferenceDescriptor reference = md.getField(field).getReference();
            if (reference != null){
                enrichedLayout.add(field+"."+"Id");
                String identityField = identificationRules.getIdentityField(reference.getTargetType());
                if (identityField != null){
                    enrichedLayout.add(field+"."+identityField);
                }

            }
        }*/

        String layoutStr = StringUtils.toCommaSeparatedString(enrichedLayout,false);
        String uri = "ems/" + entityType + "?layout=" + layoutStr;
        if (filter != null){
            uri += "&filter="+filter.getFilterString();
        }
        uri+="&size=4000";

        HttpURLConnection connection = server.buildConnection(uri);

        try {
            List<EntityInstance> results = new ArrayList<EntityInstance>();

            String resultsJson =  ConnectionUtils.connectAndGetResponse(connection);


            JSONObject json = new JSONObject(resultsJson);
            String status = json.getJSONObject(META).getString(COMPLETION_STATUS);
            if (!STATUS_OK.equals(status)){
                  throw new RuntimeException("ERROR reading entity: "+connection.getURL());
            }
            JSONArray jsonArray = json.getJSONArray(ENTITIES);


            for (int i=0;i<jsonArray.length();i++){
                results.add(EntityInstanceParser.entityInstanceFromJson(jsonArray.getJSONObject(i).toString(),server));
            }

            return results;

        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }

    public List<EntityInstance> readFullLayout(String entityType,FilterBuilder filter){
        EntityTypeDescriptor descriptor = server.getMetadataAPI().getEntityDescriptor(entityType);
        List<FieldDescriptor> fields = descriptor.getFields();

        List<String> names = new ArrayList<String>();
        for (FieldDescriptor field : fields) {
            names.add(field.getName());
        }

        return readEntities(entityType, names,filter);
    }
    public List<EntityInstance> readFullLayout(String entityType){
        return readFullLayout(entityType, null);
    }


}

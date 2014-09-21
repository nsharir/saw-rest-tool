package com.hp.maas.apis;

import com.hp.maas.apis.model.metadata.EntityTypeDescriptor;
import com.hp.maas.apis.model.metadata.MetadataParser;
import com.hp.maas.utils.ConnectionUtils;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class MetadataAPI {

    private Server server;
    private Map<String,EntityTypeDescriptor> metaData = new HashMap<String, EntityTypeDescriptor>();

     MetadataAPI(Server server) {
        this.server = server;
    }


     private EntityTypeDescriptor readFromServer(String type) {
         HttpURLConnection connection = server.buildConnection("metadata/ui/entity-descriptors/"+type);

         try {
             String json = ConnectionUtils.connectAndGetResponse(connection);
             return MetadataParser.createEntityTypeDescriptor(json);
         } catch (Exception e) {
             throw  new RuntimeException(e);
         }

    }

    public EntityTypeDescriptor getEntityDescriptor(String type){
        EntityTypeDescriptor descriptor = metaData.get(type);
        if  (descriptor == null) {
               descriptor = readFromServer(type);
            metaData.put(type,descriptor);
        }
        return  descriptor;
    }

}

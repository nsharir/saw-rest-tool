package com.hp.maas.apis;

import com.hp.maas.apis.model.entity.*;
import com.hp.maas.utils.ConnectionUtils;
import com.hp.maas.utils.Log;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class EntityWriterAPI {
    public static final String OPERATION = "operation";

    public static final String OPERATION_CREATE = "CREATE";
    public static final String ENTITIES = "entities";
    public static final String OPERATION_UPDATE = "UPDATE";
    public static final String OPERATION_DELETE = "DELETE";

    private static final String COMPLETION_STATUS = "completion_status";

    private Server server;

     EntityWriterAPI(Server server) {
        this.server = server;
    }

    public EntityBulkResult insertEntities(List<EntityInstance> entities){
        HttpURLConnection connection = server.buildPostConnection("ems/bulk");
        EntityBulkResult bulkResult = bulkInvoke(entities, OPERATION_CREATE, connection);
        if (bulkResult.getFailures().size() > 0){
            for (BulkResultEntry entry : bulkResult.getFailures()) {
                Log.error("Failed to insert entity: "+entry);
            }
            throw new RuntimeException("Failed to run insert bulk..."+bulkResult.getFailures().size()+" out of "+entities.size()+" failed to be created");
        }
        return bulkResult;
    }

    public EntityBulkResult insertEntity(EntityInstance entity){
        ArrayList<EntityInstance> list = new ArrayList<EntityInstance>();
        list.add(entity);
        return  insertEntities(list);
    }

    public EntityBulkResult updateEntity(EntityInstance entity){
        ArrayList<EntityInstance> list = new ArrayList<EntityInstance>();
        list.add(entity);
        return  updateEntities(list);
    }

    public EntityBulkResult updateEntities(List<EntityInstance> entities){
        HttpURLConnection connection = server.buildPostConnection("ems/bulk");
        EntityBulkResult bulkResult = bulkInvoke(entities, OPERATION_UPDATE, connection);
        if (bulkResult.getFailures().size() > 0){
            for (BulkResultEntry entry : bulkResult.getFailures()) {
                Log.error("Failed to update entity: "+entry);
            }
            throw new RuntimeException("Failed to run update bulk..."+bulkResult.getFailures().size()+" out of "+entities.size()+" failed to be updated");
        }

        return bulkResult;
    }

    public EntityBulkResult deleteEntity(EntityInstance entity){
        ArrayList<EntityInstance> list = new ArrayList<EntityInstance>();
        list.add(entity);
        return  deleteEntities(list);
    }

    public EntityBulkResult deleteEntities(List<EntityInstance> entities){
        HttpURLConnection connection = server.buildPostConnection("ems/bulk");
        EntityBulkResult bulkResult = bulkInvoke(entities, OPERATION_DELETE, connection);
        if (bulkResult.getFailures().size() > 0){
            for (BulkResultEntry entry : bulkResult.getFailures()) {
                Log.error("Failed to delete entity: "+entry);
            }
            throw new RuntimeException("Failed to run delete bulk..."+bulkResult.getFailures().size()+" out of "+entities.size()+" failed to be deleted");
        }

        return bulkResult;
    }

    private EntityBulkResult bulkInvoke(List<EntityInstance> entities, String operation, HttpURLConnection connection) {

        try {

            JSONObject json = new JSONObject();
            json.put(OPERATION, operation);

            for (EntityInstance entity : entities) {
                json.append(ENTITIES, new JSONObject(EntityInstanceParser.entityInstanceToJson(entity)));
            }

            Log.log("Bulk - "+operation+": " + json);
            connection.getOutputStream().write(json.toString().getBytes("UTF8"));

            String responseJson = ConnectionUtils.connectAndGetResponse(connection);


            return BulkResultsParser.toEntityBulkResult(responseJson, server);

        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }

}

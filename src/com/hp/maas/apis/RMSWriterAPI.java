package com.hp.maas.apis;

import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.apis.model.rms.RMSInstance;
import com.hp.maas.utils.ConnectionUtils;
import org.json.JSONArray;
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
public class RMSWriterAPI {
    public static final String COMPLETION_STATUS = "completionStatus";
    public static final String STATUS_OK = "OK";
    public static final String RESOURCE_IDS = "resourceIds";

    private Server server;

     RMSWriterAPI(Server server) {
        this.server = server;
    }

    public void updateResource(RMSInstance resource){


        if (!resource.getContent().keySet().contains("id")){
            throw new RuntimeException("Can't update a resource without an id: "+ resource);
        }

        String id = resource.getContent().getString("id");


        String uri = "rms/" + resource.getType() +"/" + id;


        HttpURLConnection connection = server.buildPutConnection(uri);

        try {

            connection.getOutputStream().write(resource.getContent().toString().getBytes("UTF8"));

            String resultsJson =  ConnectionUtils.connectAndGetResponse(connection);


            JSONObject json = new JSONObject(resultsJson);


            String status = json.getString(COMPLETION_STATUS);
            if (!STATUS_OK.equals(status)){
                  throw new RuntimeException("ERROR updating resource: "+connection.getURL()+". resource:\n"+resource);
            }

        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }


    public void insertResource(RMSInstance resource){

        if (resource.getContent().keySet().contains("id")){
            throw new RuntimeException("Can't insert resource with an existing id. id must be empty");
        }

        String uri = "rms/" + resource.getType();


        HttpURLConnection connection = server.buildPostConnection(uri);

        try {

            connection.getOutputStream().write(resource.getContent().toString().getBytes("UTF8"));

            String resultsJson =  ConnectionUtils.connectAndGetResponse(connection);


            JSONObject json = new JSONObject(resultsJson);

            String status = json.getString(COMPLETION_STATUS);
            if (!STATUS_OK.equals(status)){
                throw new RuntimeException("ERROR updating resource: "+connection.getURL()+". resource:\n"+resource);
            }

            JSONArray jsonArray = json.getJSONArray(RESOURCE_IDS);
            String newId = jsonArray.getString(0);
            resource.getContent().put("id",newId);

        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }

    public void deleteResource(RMSInstance resource){

        if (!resource.getContent().keySet().contains("id")){
            throw new RuntimeException("Can't update a resource without an id: "+ resource);
        }

        String id = resource.getContent().getString("id");

        String uri = "rms/" + resource.getType() +"/" + id;


        HttpURLConnection connection = server.buildDeleteConnection(uri);

        try {

            String resultsJson =  ConnectionUtils.connectAndGetResponse(connection);


            JSONObject json = new JSONObject(resultsJson);

            String status = json.getString(COMPLETION_STATUS);
            if (!STATUS_OK.equals(status)){
                throw new RuntimeException("ERROR updating resource: "+connection.getURL()+". resource:\n"+resource);
            }

        } catch (IOException e) {
            throw  new RuntimeException(e);
        }

    }



}

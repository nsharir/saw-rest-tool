package com.hp.maas.apis;

import com.hp.maas.apis.model.rms.RMSInstance;
import com.hp.maas.apis.model.query.FilterBuilder;
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
public class RMSReaderAPI {
    public static final String RESULTS = "results";
    public static final String COMPLETION_STATUS = "completionStatus";
    public static final String STATUS_OK = "OK";
    private Server server;

     RMSReaderAPI(Server server) {
        this.server = server;
    }

    public List<RMSInstance> readResources(String resourceType){
        return  readResources(resourceType, null,null);
    }

    public List<RMSInstance> readResources(String resourceType,FilterBuilder filter){
        return  readResources(resourceType, filter,null);
    }

    public List<RMSInstance> readResources(String resourceType , FilterBuilder filter , Integer pageSize){

        String uri = "rms/" + resourceType + "?";

        if (filter != null){
            uri += "&filter="+filter.getFilterString();
        }
        int size = 2000;

        if (pageSize != null){
            size = pageSize;
        }
        uri+="&size="+size;

        HttpURLConnection connection = server.buildConnection(uri);

        try {
            List<RMSInstance> results = new ArrayList<RMSInstance>();

            String resultsJson =  ConnectionUtils.connectAndGetResponse(connection);


            JSONObject json = new JSONObject(resultsJson);

            String status = json.getString(COMPLETION_STATUS);
            if (!STATUS_OK.equals(status)){
                  throw new RuntimeException("ERROR reading resource from RMS: "+connection.getURL());
            }

            JSONArray jsonArray = json.getJSONArray(RESULTS);


            for (int i=0;i<jsonArray.length();i++){
                results.add(new RMSInstance(resourceType,jsonArray.getJSONObject(i)));
            }



            return results;

        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }




}

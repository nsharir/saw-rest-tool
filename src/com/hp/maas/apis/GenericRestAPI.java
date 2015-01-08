package com.hp.maas.apis;

import com.hp.maas.apis.model.RequestPayload;
import com.hp.maas.apis.model.rms.RMSInstance;
import com.hp.maas.utils.ConnectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sharir on 06/01/2015.
 */
public class GenericRestAPI {

    private Server server;

    public GenericRestAPI(Server server) {
        this.server = server;
    }

    public  JSONObject executeGet(String uri){
        HttpURLConnection connection = server.buildConnection(uri);

        try {

            String resultsJson =  ConnectionUtils.connectAndGetResponse(connection);

            return new JSONObject(resultsJson);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}

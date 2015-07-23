package com.hp.maas.apis;

import com.hp.maas.apis.model.rb.ResourceBundleEntry;
import com.hp.maas.apis.model.rb.ResourceBundleParser;
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
public class TenantSettingsAPI {

    private Server server;

    private List<String> supportedLocales;


     TenantSettingsAPI(Server server) {
        this.server = server;
    }

    public JSONObject getSetting(String key){
        return server.getGenericRestAPI().executeGet("TenantSettings/settings/"+key+"?TENANTID=" + server.getTenantId());
    }

    public void setSetting(String key, JSONObject value){
        HttpURLConnection connection = server.buildPutConnection("TenantSettings/settings/" + key + "?TENANTID=" + server.getTenantId());

        try {
            connection.getOutputStream().write(value.toString(1).getBytes("UTF8"));
            ConnectionUtils.connectAndGetResponse(connection);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}

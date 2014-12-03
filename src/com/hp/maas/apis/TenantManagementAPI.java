package com.hp.maas.apis;

import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.apis.model.tenatManagment.TenantParser;
import com.hp.maas.utils.ConnectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sharir on 03/12/2014.
 */
public class TenantManagementAPI {

    private Server server ;

    public TenantManagementAPI(Server server) {
        this.server = server;
    }

    public List<Tenant> getAllTenants(){

        String uri = "TenantManagement/tenants";

        HttpURLConnection connection = server.buildConnection(uri);
        List<Tenant> list = new ArrayList<Tenant>();

        try {

            String resultsJson =  ConnectionUtils.connectAndGetResponse(connection);

            JSONArray json = new JSONArray(resultsJson);

            for (int i=0;i<json.length();i++){
                JSONObject tenantJson = json.getJSONObject(i);
                list.add(TenantParser.toTenant(tenantJson.toString()));
            }


            return list;

        } catch (IOException e) {
            throw  new RuntimeException(e);
        }

    }


}

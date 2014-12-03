package com.hp.maas.apis.model.tenatManagment;

import org.json.JSONObject;

/**
 * Created by sharir on 03/12/2014.
 */
public class TenantParser {

    public static Tenant toTenant(String jsonText){

        JSONObject json = new JSONObject(jsonText);

        return new Tenant(json.getString("tenantId"),
                          json.getString("customerId"),
                          json.getString("state"),
                          json.getString("type"),
                          json.getString("tenantName"),
                          json.getString("version")
                );
    }
}

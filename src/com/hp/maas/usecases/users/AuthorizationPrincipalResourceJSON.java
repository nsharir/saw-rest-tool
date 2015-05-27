package com.hp.maas.usecases.users;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sharir on 28/04/2015.
 */
public class AuthorizationPrincipalResourceJSON {

    private JSONObject json;

    /*
    {
           "id":"553f42290cf29dd7cb4a16a6"
           "UserId":"ofer.karp@hp.com",
           "Roles":["Self-Service Portal User","tenantAdmin"],
           "IsSystem":false,
         }
     */

    public AuthorizationPrincipalResourceJSON(JSONObject json) {
        this.json = json;
    }

    public AuthorizationPrincipalResourceJSON() {
        this(new JSONObject());
    }

    public String getId(){
        if (json.has("id")){
            return json.getString("id");
        }
        return null;
    }

    public void setUserId(String upn){
        json.put("UserId",upn);
    }

    public String getUserId(){
        if (json.has("UserId")){
            return json.getString("UserId");
        }
        return null;
    }

    public void setSystem(boolean isSystem){
        json.put("IsSystem",isSystem);
    }

    public boolean isSystem(){
        if (json.has("IsSystem")){
            return json.getBoolean("IsSystem");
        }
        return false;
    }

    public void setRoles(List<String> roles){
        JSONArray array = new JSONArray();
        for (String role : roles) {
            array.put(role);
        }
        json.put("Roles",array);
    }

    public List<String> getRoles(){
        if (json.has("Roles")){
            JSONArray roles = json.getJSONArray("Roles");
            List<String> array = new ArrayList<String>();
            for (int i=0; i<roles.length();i++) {
               array.add(roles.getString(i));
            }
            return array;
        }
        return null;
    }

    public JSONObject getJson() {
        return json;
    }
}

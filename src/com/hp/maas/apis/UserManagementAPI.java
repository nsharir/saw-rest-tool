package com.hp.maas.apis;

import com.hp.maas.apis.model.rms.RMSInstance;
import com.hp.maas.utils.ConnectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class UserManagementAPI {

    private Server server;

     UserManagementAPI(Server server) {
        this.server = server;
    }



    public void insertUser(String userName, String userPassword){

        String uri = "ums/Person";


        HttpURLConnection connection = server.buildPostConnection(uri);

        try {

            JSONObject jsonObject = new JSONObject();
            JSONObject nameObject = new JSONObject();
            nameObject.put("familyName","");
            nameObject.put("givenName","");
            jsonObject.put("name",nameObject);
            jsonObject.put("userName",userName);
            jsonObject.put("password",userPassword);

            JSONArray phoneNumbers = new JSONArray();
            JSONObject phone = new JSONObject();
            phone.put("value",""+System.currentTimeMillis());
            phoneNumbers.put(phone);
            jsonObject.put("phoneNumbers", phoneNumbers);
            jsonObject.put("timezone", "GMT+02:00");
            jsonObject.put("locale", "en-US");
            JSONArray emails = new JSONArray();
            JSONObject email = new JSONObject();
            email.put("value",userName);
            email.put("primary",true);
            emails.put(email);
            jsonObject.put("emails", emails);
            JSONArray roles = new JSONArray();
            JSONObject role = new JSONObject();
            role.put("value","CUSTOMER_NEW_USER");
            roles.put(role);
            jsonObject.put("roles", roles);
            JSONArray tenants = new JSONArray();
            JSONObject tenant = new JSONObject();
            tenant.put("value",server.getTenantId());
            tenants.put(tenant);
            jsonObject.put("tenants", tenants);


            System.out.println(jsonObject.toString(1));
            connection.getOutputStream().write(jsonObject.toString().getBytes("UTF8"));

            System.out.println(ConnectionUtils.connectAndGetResponse(connection));



        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }


}

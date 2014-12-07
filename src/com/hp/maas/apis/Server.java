package com.hp.maas.apis;

import com.hp.maas.configuration.ConfigurationParser;
import com.hp.maas.configuration.IdentificationRules;
import com.hp.maas.utils.ConnectionUtils;
import com.hp.maas.utils.Log;

import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 9:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Server {

    private static String AUTH_URL = "auth/authentication-endpoint/authenticate/login";

    private String hostUrl;
    private String name;
    private String password;
    private String tenant;

    private String token;

    private MetadataAPI metadataAPI;
    private EntityReaderAPI entityReaderAPI;
    private EntityWriterAPI  entityWriterAPI;
    private RMSReaderAPI rmsReaderAPI;
    private RMSWriterAPI rmsWriterAPI;
    private ResourceBundleAPI resourceBundleAPI;
    private TenantManagementAPI tenantManagementAPI;

    public Server(String hostUrl, String name, String password, String tenant) {
        this.hostUrl = hostUrl;
        this.name = name;
        this.password = password;
        this.tenant = tenant;
    }

    public Server(String hostUrl, String tenant, String token) {
        this.hostUrl = hostUrl;
        this.tenant = tenant;
        this.token = token;
    }

    public void authenticate(){
        if (token == null) {
           authenticateByCredentials();
        }

        metadataAPI = new MetadataAPI(this);
        entityReaderAPI = new EntityReaderAPI(this );
        entityWriterAPI = new EntityWriterAPI(this);
        rmsReaderAPI = new RMSReaderAPI(this);
        rmsWriterAPI = new RMSWriterAPI(this);
        resourceBundleAPI = new ResourceBundleAPI(this);
        tenantManagementAPI = new TenantManagementAPI(this);

        Log.log("Auth Token: "+ token);

    }

    public String getToken() {
        return token;
    }

    private void authenticateByCredentials() {
        try {
            URL url = new URL(hostUrl+AUTH_URL+"?login="+name+"&password="+password);

            Log.log("Auth URL: " + url.toString());

            HttpURLConnection con = ConnectionUtils.openConnection(url);
            con.setRequestMethod("GET");

            token = ConnectionUtils.connectAndGetResponse(con);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public HttpURLConnection buildPostConnection(String uri){
        HttpURLConnection connection = buildConnectionWithPayload(uri);
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public HttpURLConnection buildDeleteConnection(String uri){
        HttpURLConnection connection = buildConnectionWithPayload(uri);
        try {
            connection.setRequestMethod("DELETE");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public HttpURLConnection buildPutConnection(String uri){
        HttpURLConnection connection = buildConnectionWithPayload(uri);
        try {
            connection.setRequestMethod("PUT");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public HttpURLConnection buildConnectionWithPayload(String uri){
        HttpURLConnection connection = buildConnection(uri);
        try {
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.addRequestProperty("Accept", "application/json, text/plain, */*");
            return connection;
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpURLConnection buildConnection(String uri){
        try {
            URL url = new URL(hostUrl+"rest/"+tenant+"/"+uri);
            HttpURLConnection connection = ConnectionUtils.openConnection(url);
            connection.setRequestProperty("Cookie", "LWSSO_COOKIE_KEY="+ token+";"+"TENANTID="+tenant+";" );
            return connection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MetadataAPI getMetadataAPI() {
        return metadataAPI;
    }

    public EntityReaderAPI getEntityReaderAPI() {
        return entityReaderAPI;
    }

    public EntityWriterAPI getEntityWriterAPI() {
        return entityWriterAPI;
    }

    public RMSReaderAPI getRmsReaderAPI() {
        return rmsReaderAPI;
    }

    public RMSWriterAPI getRmsWriterAPI() {
        return rmsWriterAPI;
    }

    public ResourceBundleAPI getResourceBundleAPI() {
        return resourceBundleAPI;
    }

    public TenantManagementAPI getTenantManagementAPI() {
        return tenantManagementAPI;
    }

    public String getTenantId() {
        return tenant;
    }
}

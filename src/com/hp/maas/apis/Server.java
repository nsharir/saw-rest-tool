package com.hp.maas.apis;

import com.hp.maas.configuration.ConfigurationParser;
import com.hp.maas.configuration.IdentificationRules;
import com.hp.maas.utils.ConnectionUtils;
import com.hp.maas.utils.Log;
import com.hp.maas.utils.executers.multiTenant.MultiTenantExecutor;
import com.hp.maas.utils.executers.multiTenant.SingleTenantExecutor;
import com.hp.maas.utils.executers.multiTenant.TenantCommand;
import com.hp.maas.utils.executers.multiTenant.TenantFilter;
import com.hp.maas.utils.executers.reporters.Reporter;

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
    private TenantSettingsAPI tenantSettingsAPI;
    private TenantManagementAPI tenantManagementAPI;
    private UserManagementAPI userManagementAPI;
    private GenericRestAPI genericRestAPI;

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

    public Server fork(String tenatId){
        Server server = new Server(hostUrl, tenatId,getToken());
        server.authenticate();
        return server;
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
        userManagementAPI = new UserManagementAPI(this);
        genericRestAPI = new GenericRestAPI(this);
        tenantSettingsAPI = new TenantSettingsAPI(this);

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

    public UserManagementAPI getUserManagementAPI() {
        return userManagementAPI;
    }

    public GenericRestAPI getGenericRestAPI() {
        return genericRestAPI;
    }

    public TenantSettingsAPI getTenantSettingsAPI() {
        return tenantSettingsAPI;
    }

    public String getTenantId() {
        return tenant;
    }

    public String getServerUrl() {
        return hostUrl;
    }

    public void executeCommand(TenantCommand tc,Reporter reporter){
        SingleTenantExecutor executor = new SingleTenantExecutor(this,reporter);
        executor.run(tc);
    }

    public void executeCommand(TenantCommand tc){
        SingleTenantExecutor executor = new SingleTenantExecutor(this);
        executor.run(tc);
    }

    public void executeCommandMultiTenant(TenantCommand tc, TenantFilter filter, Reporter reporter){
        MultiTenantExecutor executor = new MultiTenantExecutor(this,filter,reporter);
        executor.run(tc);
    }

    public void executeCommandMultiTenant(TenantCommand tc, TenantFilter filter){
        MultiTenantExecutor executor = new MultiTenantExecutor(this,filter);
        executor.run(tc);
    }
}

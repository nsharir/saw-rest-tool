package com.hp.maas.apis;

import com.hp.maas.apis.model.rb.ResourceBundleEntry;
import com.hp.maas.apis.model.rb.ResourceBundleParser;
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
public class ResourceBundleAPI {

    public static final String LOCALES = "Locales";
    public static final String VALUES = "Values";
    private Server server;

    private List<String> supportedLocales;


     ResourceBundleAPI(Server server) {
        this.server = server;
    }

    public List<String> getSupportedLocales() {
        if (supportedLocales == null){
            supportedLocales = readLocales();
        }
        return supportedLocales;
    }

    private List<String> readLocales() {

        String uri = "l10n/locales";

        HttpURLConnection connection = server.buildConnection(uri);

        try {
            List<String> results = new ArrayList<String>();

            String resultsJson =  ConnectionUtils.connectAndGetResponse(connection);


            JSONObject json = new JSONObject(resultsJson);

            JSONArray jsonArray = json.getJSONArray(LOCALES);


            for (int i=0;i<jsonArray.length();i++){
                results.add(jsonArray.getString(i));
            }

            return results;

        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }


    public ResourceBundleEntry getResourceBundleEntry(String bundle, String key) {

        String uri = "l10n/bundle/"+bundle+"/resource/"+key;

        HttpURLConnection connection = server.buildConnection(uri);

        try {
            List<String> results = new ArrayList<String>();

            String resultsJson =  ConnectionUtils.connectAndGetResponse(connection);


            return ResourceBundleParser.parseEntry(resultsJson);

        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }


}

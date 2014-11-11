package com.hp.maas.apis.model.rb;

import com.sun.deploy.net.proxy.AutoProxyScript;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: sharir
 * Date: 11/11/14
 * Time: 13:34
 * To change this template use File | Settings | File Templates.
 */
public class ResourceBundleParser {

    public static final String VALUES = "Values";
    public static final String KEY = "Key";
    public static final String BUNDLE_NAME = "BundleName";

    public static ResourceBundleEntry parseEntry(String jsonText){
        JSONObject json = new JSONObject(jsonText);
        JSONObject translations = json.getJSONObject(VALUES);
        String bundle = json.getString(BUNDLE_NAME);
        Map<String, String> map = new HashMap<String, String>();

        Set set = translations.keySet();
        for (Object key : set) {
            String keyStr = (String) key;
            map.put(keyStr, translations.getString(keyStr));
        }


        return new ResourceBundleEntry(bundle,json.getString(KEY),map);
    }

    public static ResourceBundle parseBundle(String resultsJson) {
        JSONObject json = new JSONObject(resultsJson);
        String name = json.getString("Name");
        String locale = json.getString("Locale");
        JSONObject resources = json.getJSONObject("Resources");
        Map<String, String > map;
        Map<String, ResourceBundleEntry> entriesMap = new HashMap<String, ResourceBundleEntry>();

        Set set = resources.keySet();
        for (Object key : set) {
            String keyStr = (String) key;
            map = new HashMap<String, String>();
            map.put(locale,resources.getString(keyStr));

            entriesMap.put(keyStr, new ResourceBundleEntry(name,keyStr,map));
        }
        return new ResourceBundle(name, entriesMap);
    }

    public static String entryToJson(ResourceBundleEntry entry) {
        JSONObject json = new JSONObject();
        JSONObject values = new JSONObject();

        Map<String, String> map = entry.getTranslationMap();
        for (Map.Entry<String, String> lang : map.entrySet()) {
            values.put(lang.getKey(),lang.getValue());
        }
        json.put(VALUES, values);

        return json.toString(1);
    }
}

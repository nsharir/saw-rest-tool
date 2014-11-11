package com.hp.maas.apis.model.rb;

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

    public static ResourceBundleEntry parseEntry(String jsonText){
        JSONObject json = new JSONObject(jsonText);
        JSONObject translations = json.getJSONObject(VALUES);
        Map<String, String> map = new HashMap<String, String>();

        Set set = translations.keySet();
        for (Object key : set) {
            String keyStr = (String) key;
            map.put(keyStr, translations.getString(keyStr));
        }


        return new ResourceBundleEntry(json.getString(KEY),map);
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

            entriesMap.put(keyStr, new ResourceBundleEntry(keyStr,map));
        }
        return new ResourceBundle(name, entriesMap);
    }
}

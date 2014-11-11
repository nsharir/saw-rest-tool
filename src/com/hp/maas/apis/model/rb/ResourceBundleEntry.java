package com.hp.maas.apis.model.rb;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: sharir
 * Date: 11/11/14
 * Time: 13:18
 * To change this template use File | Settings | File Templates.
 */
public class ResourceBundleEntry {

    private String bundle;
    private String key;
    private Map<String,String> valuesMap;

    public ResourceBundleEntry(String bundle,String key, Map<String, String> valuesMap) {
        this.key = key;
        this.valuesMap = valuesMap;
        this.bundle = bundle;
    }

    public String getBundle() {
        return bundle;
    }

    public String getKey() {
        return key;
    }

    public Map<String, String> getTranslationMap() {
        return valuesMap;
    }

    public String getLabel(String lang) {
        return valuesMap.get(lang);
    }

    public String setLabel(String lang, String label) {
        return valuesMap.put(lang, label);
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("Key = '").append(key).append("' ; Translations: {");

        Map<String, String> translationMap = this.getTranslationMap();

        Set<Map.Entry<String, String>> entries = translationMap.entrySet();
        for (Map.Entry<String, String> rb : entries) {
            str.append(rb.getKey()).append(":'").append(rb.getValue()).append("'").append(" , ");
        }

        str.append("}");
        return str.toString();
    }
}

package com.hp.maas.apis.model.rb;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sharir
 * Date: 11/11/14
 * Time: 13:18
 * To change this template use File | Settings | File Templates.
 */
public class ResourceBundle {
    private String name;
    private Map<String,ResourceBundleEntry> values;

    public ResourceBundle(String name, Map<String, ResourceBundleEntry> values) {
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public Map<String, ResourceBundleEntry> getEntries() {
        return values;
    }
}

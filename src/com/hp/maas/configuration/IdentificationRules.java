package com.hp.maas.configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/13/14
 * Time: 12:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class IdentificationRules {
    private Map<String,String> identityMap = new HashMap<String, String>();

    public IdentificationRules(Map<String, String> identityMap) {
        this.identityMap = identityMap;
    }

    public String getIdentityField(String type){
        return identityMap.get(type);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("{Identification rules: [\n");
        for (Map.Entry<String, String> entry : identityMap.entrySet()) {
            s.append("     (").append(entry.getKey()).append("-->").append(entry.getValue()).append(")\n");
        }
        s.append("]}");

        return s.toString();
    }
}

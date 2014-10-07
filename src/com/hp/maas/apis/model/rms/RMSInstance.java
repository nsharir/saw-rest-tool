package com.hp.maas.apis.model.rms;

import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: sharir
 * Date: 07/10/14
 * Time: 08:23
 * To change this template use File | Settings | File Templates.
 */
public class RMSInstance {
    private String type;
    private JSONObject content;

    public RMSInstance(String type, JSONObject content) {
        this.type = type;
        this.content = content;
    }


    public String getType() {
        return type;
    }

    public JSONObject getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content.toString(1);
    }
}

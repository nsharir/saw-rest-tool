package com.hp.maas.jsons.forms;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by sharir on 27/11/2014.
 */
public class Form {

    private String id;
    private String name;
    private String entityType;
    private List<FormSection> sections;
    private JSONObject originalFormJson;

    public JSONObject getOriginalFormJson() {
        return originalFormJson;
    }

    public void setOriginalFormJson(JSONObject originalFormJson) {
        this.originalFormJson = originalFormJson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public List<FormSection> getSections() {
        return sections;
    }

    public void setSections(List<FormSection> sections) {
        this.sections = sections;
    }

    @Override
    public String toString() {
        return "Form{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", entityType='" + entityType + '\'' +
                ", sections=" + sections +
                '}';
    }
}

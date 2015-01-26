package com.hp.maas.apis.model.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class EntityInstance {

    private String type;
    private Map<String,Object> fields;
    private Map<String,EntityInstance> relatedEntities = new HashMap<String, EntityInstance>();

      EntityInstance(String type, Map<String, Object> fields, Map<String,EntityInstance> relatedEntities) {
        this.type = type;
        this.fields = new HashMap<String, Object>(fields);
        this.relatedEntities = relatedEntities;
    }

    public EntityInstance(EntityInstance ent) {
        this(ent.type, new HashMap<String, Object>(ent.fields),new HashMap<String, EntityInstance>(ent.relatedEntities));
    }

    public Collection<String> getFieldsNames() {
        return Collections.unmodifiableCollection(fields.keySet());
    }

    public Object getFieldValue(String fieldName){
        return fields.get(fieldName);
    }

    public boolean hasField(String fieldName){
        return fields.get(fieldName) != null;
    }

    public void setFieldValue(String fieldName, Object value){
         fields.put(fieldName,value);
    }

    public String getType() {
        return type;
    }

    public Object removeField(String fieldName){
        return fields.remove(fieldName);
    }

    public EntityInstance getRelatedEntity(String relationFieldName){
        return relatedEntities.get(relationFieldName);
    }

    @Override
    public String toString() {
        return EntityInstanceParser.entityInstanceToJson(this);
    }
}

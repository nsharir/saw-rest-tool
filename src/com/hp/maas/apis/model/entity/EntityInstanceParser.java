package com.hp.maas.apis.model.entity;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.metadata.EntityTypeDescriptor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 9:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class EntityInstanceParser {

    private static final String ENTITY_TYPE = "entity_type";
    private static final String PROPERTIES = "properties";
    public static final String RELATED_PROPERTIES = "related_properties";
    public static final String COMPLEX_TYPE_PROPERTIES = "complexTypeProperties";
    public static final String ATTRIBUTE_NAME = "AttributeName";
    public static final String VALUE = "Value";

    public static EntityInstance entityInstanceFromJson(String json,Server server){

        Map<String, Object> fields = new HashMap<String, Object>();
        String type;

        JSONObject obj = new JSONObject(json);
        type = obj.getString(ENTITY_TYPE);


        EntityTypeDescriptor md = server.getMetadataAPI().getEntityDescriptor(type);

        JSONObject properties = obj.getJSONObject(PROPERTIES);

        Set<String> set = properties.keySet();
        for (String key : set) {
            fields.put(key, properties.get(key));
        }

        Map<String,EntityInstance> relatedEntities = new HashMap<String, EntityInstance>();

        JSONObject related = obj.getJSONObject(RELATED_PROPERTIES);
        Set relationArray = related.keySet();
        for (Object relatedProperty : relationArray) {
            String relatedPropertyName = (String) relatedProperty;
            JSONObject propertiesCollection = (JSONObject) related.get(relatedPropertyName);
            Set<String> relatedSet = propertiesCollection.keySet();
            Map<String, Object> relatedFieldsMap = new HashMap<String, Object>();
            for (String key : relatedSet) {
                relatedFieldsMap.put(key, propertiesCollection.getString(key));
            }
            EntityInstance relatedInstance = new EntityInstance(md.getField(relatedPropertyName).getReference().getTargetType(),relatedFieldsMap,new HashMap<String, EntityInstance>());
            relatedEntities.put(relatedPropertyName,relatedInstance);
        }

        return new EntityInstance(type,fields,relatedEntities);
    }

    public static String entityInstanceToJson(EntityInstance entity){

        JSONObject properties = new JSONObject();
        for (String field : entity.getFieldsNames()) {
            properties.put(field, entity.getFieldValue(field));
        }

        JSONObject obj = new JSONObject();
        obj.put(ENTITY_TYPE, entity.getType());
        obj.put(PROPERTIES, properties);

        return obj.toString();
    }

    public static EntityInstance parseRequestDefaultValuesComplexType(String jsonText){
        if (jsonText == null){
            return null;
        }
        JSONObject obj = new JSONObject(jsonText);
        JSONArray jsonArray = obj.getJSONArray(COMPLEX_TYPE_PROPERTIES);
        if (jsonArray !=null && jsonArray.length() > 0){
            Map<String,Object> fields = new HashMap<String, Object>();
            for (int i=0;i<jsonArray.length();i++){
                JSONObject defaults = jsonArray.getJSONObject(i);
                defaults=defaults.getJSONObject(PROPERTIES);
                fields.put(defaults.getString(ATTRIBUTE_NAME),defaults.getString(VALUE));
            }

            return new EntityInstance("Request",fields, null);
        }
        return null;

    }
}

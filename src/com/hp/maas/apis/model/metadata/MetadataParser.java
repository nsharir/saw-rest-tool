package com.hp.maas.apis.model.metadata;

import com.hp.maas.apis.model.entity.ReferenceType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetadataParser {


    private static final String TYPE_NAME = "name";
    private static final String TYPE_PROPERTY_DESCRIPTORS = "property_descriptors";

    private static final String FIELD_NAME = "name";
    private static final String FIELD_LOGICAL_TYPE = "logical_type";
    private static final String RELATION_DESCRIPTOR = "relation_descriptor";
    private static final String SECOND_ENDPOINT_ENTITY_NAME = "second_endpoint_entity_name";

    public static EntityTypeDescriptor createEntityTypeDescriptor(String jsonStr){
        JSONObject jsonObj = new JSONObject(jsonStr);

        String typeName = jsonObj.getString(TYPE_NAME);
        JSONArray propDescriptors = jsonObj.getJSONArray(TYPE_PROPERTY_DESCRIPTORS);

        List<FieldDescriptor> fields = new ArrayList<FieldDescriptor>();

        for (int i=0; i< propDescriptors.length();i++){

            JSONObject prop = propDescriptors.getJSONObject(i);
            String name = prop.getString(FIELD_NAME);
            String logicalType = prop.getString(FIELD_LOGICAL_TYPE);
            EntityReferenceDescriptor referenceDescriptor = createReferenceDescriptor(prop, typeName);


            FieldDescriptor fieldDescriptor = new FieldDescriptor(name, logicalType,referenceDescriptor);
            fields.add(fieldDescriptor);
        }

        return new EntityTypeDescriptor(typeName,fields);
    }

    private static  EntityReferenceDescriptor createReferenceDescriptor(JSONObject property, String type){
        String name = property.getString(FIELD_NAME);
        String logicalType = property.getString(FIELD_LOGICAL_TYPE);


        if ("ENTITY_LINK".equals(logicalType)){
            String referenceTo = property.getJSONObject(RELATION_DESCRIPTOR).getString(SECOND_ENDPOINT_ENTITY_NAME);
            return new EntityReferenceDescriptor(type,referenceTo,name, ReferenceType.ENTITY_LINK);
        }

        return null;
    }
}

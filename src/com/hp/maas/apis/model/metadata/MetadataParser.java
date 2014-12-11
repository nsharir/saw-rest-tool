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
    private static final String TYPE_RELATIONS_DESCRIPTORS = "relation_descriptors";

    private static final String FIELD_NAME = "name";
    private static final String FIELD_LOGICAL_TYPE = "logical_type";
    private static final String HIDDEN = "hidden";
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
            Boolean isHidden = prop.getBoolean(HIDDEN);

            String domain = prop.getString("domain");
            String localized_label_key = prop.getString("localized_label_key");

            Boolean system = prop.getBoolean("system");
            Boolean searchable = prop.getBoolean("searchable");
            Boolean sortable = prop.getBoolean("sortable");
            Boolean text_searchable = prop.getBoolean("text_searchable");
            Boolean required = prop.getBoolean("required");
            Boolean readOnly = prop.getBoolean("readOnly");
            Boolean unique = prop.getBoolean("unique");

            List<String> tags = new ArrayList<String>();

            JSONArray jsonArray = prop.getJSONArray("tags");

            for (int t=0;t<jsonArray.length();t++){
                tags.add(jsonArray.getString(t));
            }



            EntityReferenceDescriptor referenceDescriptor = createReferenceDescriptor(prop, typeName);


            FieldDescriptor fieldDescriptor = new FieldDescriptor(name,logicalType,isHidden,referenceDescriptor,domain,localized_label_key,system,searchable,sortable,text_searchable,required,readOnly,unique,tags);
            fields.add(fieldDescriptor);
        }


        JSONArray relationsDescriptors = jsonObj.getJSONArray(TYPE_RELATIONS_DESCRIPTORS);

        List<RelationDescriptor> relations = new ArrayList<RelationDescriptor>();

        for (int i=0; i< relationsDescriptors.length();i++){

            JSONObject prop = relationsDescriptors.getJSONObject(i);
            relations.add(new RelationDescriptor(prop.getString("name"),prop.getString("cardinality")));
        }


        return new EntityTypeDescriptor(typeName,fields,relations);
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

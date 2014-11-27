package com.hp.maas.apis.model.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class EntityTypeDescriptor {

    private String name;
    private List<FieldDescriptor> fields;
    private Map<String,FieldDescriptor> fieldMap;
    private List<RelationDescriptor> relations;
    private Map<String,RelationDescriptor> relationsMap;
    private String identificationFieldName;

    EntityTypeDescriptor(String name, List<FieldDescriptor> fields , List<RelationDescriptor> relations){
        this.name = name;
        this.identificationFieldName = identificationFieldName;
        this.fields = new ArrayList<FieldDescriptor>(fields);
        fieldMap = new HashMap<String, FieldDescriptor>();
        for (FieldDescriptor field : fields) {
            fieldMap.put(field.getName(), field);
        }

        this.relations = new ArrayList<RelationDescriptor>(relations);
        relationsMap = new HashMap<String, RelationDescriptor>();
        for (RelationDescriptor relation : relations) {
            relationsMap.put(relation.getName(), relation);
        }
    }

    public List<FieldDescriptor> getFields() {
        return fields;
    }

    public FieldDescriptor getField(String fieldName){
        return fieldMap.get(fieldName);
    }

    public List<RelationDescriptor> getRelations() {
        return relations;
    }

    public RelationDescriptor getRelation(String relationName){
        return relationsMap.get(relationName);
    }

    @Override
    public String toString() {
        return "EntityTypeDescriptor{" +
                "name='" + name + '\'' +
                ", fields=" + fields +
                ", fieldMap=" + fieldMap +
                ", relations=" + relations +
                ", relationsMap=" + relationsMap +
                '}';
    }
}

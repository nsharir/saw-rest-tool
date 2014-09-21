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
    private String identificationFieldName;

    EntityTypeDescriptor(String name, List<FieldDescriptor> fields){
        this.name = name;
        this.identificationFieldName = identificationFieldName;
        this.fields = new ArrayList<FieldDescriptor>(fields);
        fieldMap = new HashMap<String, FieldDescriptor>();
        for (FieldDescriptor field : fields) {
            fieldMap.put(field.getName(), field);
        }
    }

    public List<FieldDescriptor> getFields() {
        return fields;
    }

    public FieldDescriptor getField(String fieldName){
        return fieldMap.get(fieldName);
    }



    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("{Type: ").append(name).append(", Fields: [");
        for (FieldDescriptor field : fields) {
            str.append(field);
        }
        str.append("]}");
        return str.toString();
    }
}

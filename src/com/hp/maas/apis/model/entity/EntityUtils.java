package com.hp.maas.apis.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/14/14
 * Time: 11:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class EntityUtils {
    public static List<EntityInstance> clone(List<EntityInstance> entityInstances){
        List<EntityInstance> cloned = new ArrayList<EntityInstance>();
        for (EntityInstance instance : cloned) {
            cloned.add(new EntityInstance(instance));
        }
        return cloned;
    }

    public static void merge(List<EntityInstance> insertList, Map<Object,EntityInstance> newEntites, String identityField) {

        for (int i=0;i<insertList.size();i++){
            EntityInstance entityResult = insertList.get(i);
            Object identityValue = entityResult.getFieldValue(identityField);
            if (identityValue == null){
                throw new RuntimeException("Record of type "+entityResult.getType()+" and Id "+entityResult.getFieldValue("Id")+" Do not have a value in identity field named "+identityField);
            }
            EntityInstance  newEntity = newEntites.get(identityValue);

            for (String field : newEntity.getFieldsNames()) {
                entityResult.setFieldValue(field, newEntity.getFieldValue(field));
            }
        }
    }
}

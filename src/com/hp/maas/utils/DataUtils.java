package com.hp.maas.utils;

import com.hp.maas.apis.model.entity.EntityInstance;
import com.hp.maas.apis.model.entity.EntityInstanceParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/13/14
 * Time: 11:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataUtils {

    public static void bulkFieldRemoval(List<EntityInstance> entities, String fieldName){
        for (EntityInstance entity : entities) {
            entity.removeField(fieldName);
        }
    }

    public static void bulkFieldUpdate(List<EntityInstance> entities, String fieldName, String value){
        for (EntityInstance entity : entities) {
            entity.setFieldValue(fieldName, value);
        }
    }



}

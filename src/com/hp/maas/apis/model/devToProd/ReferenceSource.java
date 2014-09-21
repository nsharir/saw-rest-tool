package com.hp.maas.apis.model.devToProd;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/14/14
 * Time: 9:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReferenceSource {
    String entityType;
    String entityId;
    String fieldName;

    public ReferenceSource(String entityType, String entityId, String fieldName) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.fieldName = fieldName;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String toString() {
        return "ReferenceSource{" +
                "entityType='" + entityType + '\'' +
                ", entityId='" + entityId + '\'' +
                ", fieldName='" + fieldName + '\'' +
                '}';
    }
}

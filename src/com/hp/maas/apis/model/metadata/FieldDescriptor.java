package com.hp.maas.apis.model.metadata;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class FieldDescriptor {

    private String name;
    private String logicalType;
    private EntityReferenceDescriptor reference;

    FieldDescriptor(String name, String logicalType, EntityReferenceDescriptor reference) {
        this.name = name;
        this.logicalType = logicalType;
        this.reference = reference;
    }

    public EntityReferenceDescriptor getReference() {
        return reference;
    }

    public boolean hasReference() {
        return reference != null;
    }

    public String getName() {
        return name;
    }

    public String getLogicalType() {
        return logicalType;
    }

    @Override
    public String toString() {
        return "{"+name + ","+logicalType+"}";
    }




}

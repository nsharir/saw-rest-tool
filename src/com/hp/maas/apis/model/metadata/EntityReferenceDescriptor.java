package com.hp.maas.apis.model.metadata;

import com.hp.maas.apis.model.entity.ReferenceType;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/13/14
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class EntityReferenceDescriptor {

    private String sourceType;
    private String targetType;
    private String sourceFieldName;
    private ReferenceType referenceType;


     EntityReferenceDescriptor(String sourceType, String targetType, String sourceFieldName, ReferenceType referenceType) {
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.sourceFieldName = sourceFieldName;
        this.referenceType = referenceType;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getTargetType() {
        return targetType;
    }

    public String getSourceFieldName() {
        return sourceFieldName;
    }

    public ReferenceType getReferenceType() {
        return referenceType;
    }

    @Override
    public String toString() {
        return "["+sourceType+"."+sourceFieldName+" --> "+targetType+", referenceType:"+referenceType+"]";
    }
}

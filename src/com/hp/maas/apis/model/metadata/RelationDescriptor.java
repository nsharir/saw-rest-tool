package com.hp.maas.apis.model.metadata;

/**
 * Created by sharir on 27/11/2014.
 */
public class RelationDescriptor {
    private String name;
    private String cardinality;

    public RelationDescriptor(String name, String cardinality) {
        this.name = name;
        this.cardinality = cardinality;
    }

    public String getName() {
        return name;
    }

    public String getCardinality() {
        return cardinality;
    }

    @Override
    public String toString() {
        return "RelationDescriptor{" +
                "name='" + name + '\'' +
                ", cardinality='" + cardinality + '\'' +
                '}';
    }
}

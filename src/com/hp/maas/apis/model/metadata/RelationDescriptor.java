package com.hp.maas.apis.model.metadata;

/**
 * Created by sharir on 27/11/2014.
 */
public class RelationDescriptor {
    private String name;
    private String cardinality;
    private Boolean system;
    private String localized_opposite_label_key;
    private String nature;
    private String first_endpoint_entity_name;
    private String second_endpoint_entity_name;
    private String localized_label_key;

    public RelationDescriptor(String name, String cardinality, Boolean system, String localized_opposite_label_key, String nature, String first_endpoint_entity_name, String second_endpoint_entity_name, String localized_label_key) {
        this.name = name;
        this.cardinality = cardinality;
        this.system = system;
        this.localized_opposite_label_key = localized_opposite_label_key;
        this.nature = nature;
        this.first_endpoint_entity_name = first_endpoint_entity_name;
        this.second_endpoint_entity_name = second_endpoint_entity_name;
        this.localized_label_key = localized_label_key;
    }

    public String getName() {
        return name;
    }

    public String getCardinality() {
        return cardinality;
    }

    public Boolean getSystem() {
        return system;
    }

    public String getLocalized_opposite_label_key() {
        return localized_opposite_label_key;
    }

    public String getNature() {
        return nature;
    }

    public String getFirst_endpoint_entity_name() {
        return first_endpoint_entity_name;
    }

    public String getSecond_endpoint_entity_name() {
        return second_endpoint_entity_name;
    }

    public String getLocalized_label_key() {
        return localized_label_key;
    }

    @Override
    public String toString() {
        return "RelationDescriptor{" +
                "name='" + name + '\'' +
                ", cardinality='" + cardinality + '\'' +
                ", system='" + system + '\'' +
                ", localized_opposite_label_key='" + localized_opposite_label_key + '\'' +
                ", nature='" + nature + '\'' +
                ", first_endpoint_entity_name='" + first_endpoint_entity_name + '\'' +
                ", second_endpoint_entity_name='" + second_endpoint_entity_name + '\'' +
                ", localized_label_key='" + localized_label_key + '\'' +
                '}';
    }
}

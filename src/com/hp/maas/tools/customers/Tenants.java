package com.hp.maas.tools.customers;

/**
 * Created by sharir on 25/01/2015.
 */
public enum Tenants {
    ALDev ("733658382"),
    ALProd ("226830052"),
    LondonRnd("787406283")
    ;

    private String id;

    Tenants(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}


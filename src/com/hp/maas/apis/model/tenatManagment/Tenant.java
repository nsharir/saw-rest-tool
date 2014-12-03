package com.hp.maas.apis.model.tenatManagment;

/**
 * Created by sharir on 03/12/2014.
 */
public class Tenant {

    private String id;
    private String customerId ; //for SaaS
    private String state ; //Active
    private String type; // Dev
    private String tenantName;
    private String version;

    public Tenant(String id, String customerId, String state, String type, String tenantName, String version) {
        this.id = id;
        this.customerId = customerId;
        this.state = state;
        this.type = type;
        this.tenantName = tenantName;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getState() {
        return state;
    }

    public String getType() {
        return type;
    }

    public String getTenantName() {
        return tenantName;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "id='" + id + '\'' +
                ", customerId='" + customerId + '\'' +
                ", state='" + state + '\'' +
                ", type='" + type + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}

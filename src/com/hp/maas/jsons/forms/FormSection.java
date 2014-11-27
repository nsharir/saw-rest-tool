package com.hp.maas.jsons.forms;

import java.util.List;

/**
 * Created by sharir on 27/11/2014.
 */
public class FormSection {

    private String name;
    private String header;
    private String domain;
    private String resourceKey;
    private Boolean isOpen;
    private Boolean isHide;
    private String localized_label;
    private List<FormField> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Boolean getIsHide() {
        return isHide;
    }

    public void setIsHide(Boolean isHide) {
        this.isHide = isHide;
    }

    public String getLocalized_label() {
        return localized_label;
    }

    public void setLocalized_label(String localized_label) {
        this.localized_label = localized_label;
    }

    public List<FormField> getFields() {
        return fields;
    }

    public void setFields(List<FormField> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "FormSection{" +
                "name='" + name + '\'' +
                ", header='" + header + '\'' +
                ", domain='" + domain + '\'' +
                ", resourceKey='" + resourceKey + '\'' +
                ", isOpen='" + isOpen + '\'' +
                ", isHide='" + isHide + '\'' +
                ", localized_label='" + localized_label + '\'' +
                ", fields=" + fields +
                '}';
    }
}

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

    public void removeField(FormField field) {
        fields.remove(field) ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormSection that = (FormSection) o;

        if (domain != null ? !domain.equals(that.domain) : that.domain != null) return false;
        if (header != null ? !header.equals(that.header) : that.header != null) return false;
        if (isHide != null ? !isHide.equals(that.isHide) : that.isHide != null) return false;
        if (isOpen != null ? !isOpen.equals(that.isOpen) : that.isOpen != null) return false;
        if (localized_label != null ? !localized_label.equals(that.localized_label) : that.localized_label != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (resourceKey != null ? !resourceKey.equals(that.resourceKey) : that.resourceKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (header != null ? header.hashCode() : 0);
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        result = 31 * result + (resourceKey != null ? resourceKey.hashCode() : 0);
        result = 31 * result + (isOpen != null ? isOpen.hashCode() : 0);
        result = 31 * result + (isHide != null ? isHide.hashCode() : 0);
        result = 31 * result + (localized_label != null ? localized_label.hashCode() : 0);
        return result;
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

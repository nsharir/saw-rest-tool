package com.hp.maas.apis.model.metadata;

import java.util.List;

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
    private boolean hidden;

    private EntityReferenceDescriptor reference;

    private String domain;
    private String localizedLabelKey;
    private String referenceName;

    private boolean system;
    private boolean searchable;
    private boolean sortable;
    private boolean textSearchable;
    private boolean required;
    private boolean readOnly;
    private boolean unique;

    private List<String> tags;

    public FieldDescriptor(String name, String logicalType, boolean isHidden, EntityReferenceDescriptor reference, String domain, String localizedLabelKey, boolean system, boolean searchable, boolean sortable, boolean textSearchable, boolean required, boolean readOnly, boolean unique, List<String> tags,String referenceName) {
        this.name = name;
        this.logicalType = logicalType;
        this.hidden = isHidden;
        this.reference = reference;
        this.domain = domain;
        this.localizedLabelKey = localizedLabelKey;
        this.system = system;
        this.searchable = searchable;
        this.sortable = sortable;
        this.textSearchable = textSearchable;
        this.required = required;
        this.readOnly = readOnly;
        this.unique = unique;
        this.tags = tags;
        this.referenceName = referenceName;
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

    public boolean isHidden() {
        return hidden;
    }

    public String getDomain() {
        return domain;
    }

    public String getLocalizedLabelKey() {
        return localizedLabelKey;
    }

    public boolean isSystem() {
        return system;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public boolean isSortable() {
        return sortable;
    }

    public boolean isTextSearchable() {
        return textSearchable;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean isUnique() {
        return unique;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getReferenceName() {
        return referenceName;
    }

    @Override
    public String toString() {
        return "FieldDescriptor{" +
                "name='" + name + '\'' +
                ", logicalType='" + logicalType + '\'' +
                ", hidden=" + hidden +
                ", reference=" + reference +
                ", domain='" + domain + '\'' +
                ", localizedLabelKey='" + localizedLabelKey + '\'' +
                ", system=" + system +
                ", searchable=" + searchable +
                ", sortable=" + sortable +
                ", textSearchable=" + textSearchable +
                ", required=" + required +
                ", readOnly=" + readOnly +
                ", unique=" + unique +
                ", tags=" + tags +
                '}';
    }
}

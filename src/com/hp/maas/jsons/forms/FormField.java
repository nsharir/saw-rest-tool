package com.hp.maas.jsons.forms;

import org.json.JSONObject;

/**
 * Created by sharir on 27/11/2014.
 */
public class FormField {

    private String editorType;
    private String fieldSize;
    private Double labelSize;
    private Double size;
    private String modelAttribute;
    private Boolean newLine;
    private Boolean isOpposite;
    private String domain;
    private JSONObject dataProvider;

    public String getEditorType() {
        return editorType;
    }

    public void setEditorType(String editorType) {
        this.editorType = editorType;
    }

    public String getFieldSize() {
        return fieldSize;
    }

    public void setFieldSize(String fieldSize) {
        this.fieldSize = fieldSize;
    }

    public Double getLabelSize() {
        return labelSize;
    }

    public void setLabelSize(Double labelSize) {
        this.labelSize = labelSize;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public String getModelAttribute() {
        return modelAttribute;
    }

    public void setModelAttribute(String modelAttribute) {
        this.modelAttribute = modelAttribute;
    }

    public Boolean getNewLine() {
        return newLine;
    }

    public void setNewLine(Boolean newLine) {
        this.newLine = newLine;
    }

    public Boolean getIsOpposite() {
        return isOpposite;
    }

    public void setIsOpposite(Boolean isOpposite) {
        this.isOpposite = isOpposite;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public JSONObject getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(JSONObject dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public String toString() {
        return "FormField{" +
                "editorType='" + editorType + '\'' +
                ", fieldSize='" + fieldSize + '\'' +
                ", labelSize='" + labelSize + '\'' +
                ", size='" + size + '\'' +
                ", modelAttribute='" + modelAttribute + '\'' +
                ", newLine='" + newLine + '\'' +
                ", isOpposite='" + isOpposite + '\'' +
                ", domain='" + domain + '\'' +
                ", dataProvider=" + dataProvider +
                '}';
    }
}

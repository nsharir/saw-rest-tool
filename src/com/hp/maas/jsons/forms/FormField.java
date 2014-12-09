
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormField formField = (FormField) o;

        if (dataProvider != null ? !dataProvider.equals(formField.dataProvider) : formField.dataProvider != null)
            return false;
        if (domain != null ? !domain.equals(formField.domain) : formField.domain != null) return false;
        if (editorType != null ? !editorType.equals(formField.editorType) : formField.editorType != null) return false;
        if (fieldSize != null ? !fieldSize.equals(formField.fieldSize) : formField.fieldSize != null) return false;
        if (isOpposite != null ? !isOpposite.equals(formField.isOpposite) : formField.isOpposite != null) return false;
        if (labelSize != null ? !labelSize.equals(formField.labelSize) : formField.labelSize != null) return false;
        if (modelAttribute != null ? !modelAttribute.equals(formField.modelAttribute) : formField.modelAttribute != null)
            return false;
        if (newLine != null ? !newLine.equals(formField.newLine) : formField.newLine != null) return false;
        if (size != null ? !size.equals(formField.size) : formField.size != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = editorType != null ? editorType.hashCode() : 0;
        result = 31 * result + (fieldSize != null ? fieldSize.hashCode() : 0);
        result = 31 * result + (labelSize != null ? labelSize.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (modelAttribute != null ? modelAttribute.hashCode() : 0);
        result = 31 * result + (newLine != null ? newLine.hashCode() : 0);
        result = 31 * result + (isOpposite != null ? isOpposite.hashCode() : 0);
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        result = 31 * result + (dataProvider != null ? dataProvider.hashCode() : 0);
        return result;
    }
}

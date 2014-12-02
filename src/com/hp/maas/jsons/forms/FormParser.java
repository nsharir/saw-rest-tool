package com.hp.maas.jsons.forms;

import com.hp.maas.utils.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sharir on 27/11/2014.
 */
public class FormParser {


    private static class GracefulJson {
        JSONObject org;

        private GracefulJson(JSONObject org) {
            this.org = org;
        }

        public String getString(String key) {
            if (org.has(key)){
                return org.getString(key);
            }
            return null;
        }

        public JSONArray getJSONArray(String key) {
            if (org.has(key)){
                return org.getJSONArray(key);
            }
            return null;
        }

        public Double getDouble(String key) {
            if (org.has(key)){
                return org.getDouble(key);
            }
            return null;
        }

        public JSONObject getJSONObject(String key) {
            if (org.has(key)){
                return org.getJSONObject(key);
            }
            return null;
        }

        public Boolean getBoolean(String key) {
            if (org.has(key)){
                return org.getBoolean(key);
            }
            return null;
        }
    }
    public static Form toForm(JSONObject jsonOriginal) {
        Form form = new Form();

        try {
            GracefulJson json = new GracefulJson(jsonOriginal);

            form.setOriginalFormJson(jsonOriginal);
            form.setId(json.getString(FormConst.FORM.ID));
            form.setName(json.getString(FormConst.FORM.NAME));
            form.setEntityType(json.getString(FormConst.FORM.ENTITY_TYPE));

            List<FormSection> sections = new ArrayList<FormSection>();

            JSONArray jsonSections = json.getJSONArray(FormConst.FORM.SECTIONS);

            for (int i = 0; i < jsonSections.length(); i++) {
                GracefulJson jsonSection = new GracefulJson(jsonSections.getJSONObject(i));
                FormSection section = new FormSection();

                section.setName(jsonSection.getString(FormConst.SECTION.NAME));
                section.setHeader(jsonSection.getString(FormConst.SECTION.HEADER));
                section.setDomain(jsonSection.getString(FormConst.SECTION.DOMAIN));
                section.setResourceKey(jsonSection.getString(FormConst.SECTION.RESOURCE_KEY));
                section.setLocalized_label(jsonSection.getString(FormConst.SECTION.LOCALIZED_LABEL));
                section.setIsHide(jsonSection.getBoolean(FormConst.SECTION.IS_HIDE));
                section.setIsOpen(jsonSection.getBoolean(FormConst.SECTION.IS_OPEN));


                List<FormField> fields = new ArrayList<FormField>();
                JSONArray jsonFields = jsonSection.getJSONArray(FormConst.SECTION.FIELDS);

                for (int j = 0; j < jsonFields.length(); j++) {
                    GracefulJson jsonField = new GracefulJson(jsonFields.getJSONObject(j));
                    FormField field = new FormField();

                    field.setEditorType(jsonField.getString(FormConst.FIELD.EDITOR_TYPE));
                    field.setFieldSize(jsonField.getString(FormConst.FIELD.FIELD_SIZE));
                    field.setLabelSize(jsonField.getDouble(FormConst.FIELD.LABEL_SIZE));
                    field.setSize(jsonField.getDouble(FormConst.FIELD.SIZE));
                    field.setModelAttribute(jsonField.getString(FormConst.FIELD.MODEL_ATTRIBUTE));
                    field.setNewLine(jsonField.getBoolean(FormConst.FIELD.NEW_LINE));
                    field.setIsOpposite(jsonField.getBoolean(FormConst.FIELD.IS_OPPOSITE));
                    field.setDomain(jsonField.getString(FormConst.FIELD.DOMAIN));
                    field.setDataProvider(jsonField.getJSONObject(FormConst.FIELD.DATA_PROVIDER));

                    fields.add(field);
                }

                section.setFields(fields);

                sections.add(section);

            }

            form.setSections(sections);


        } catch (RuntimeException e) {
            Log.error("Error parsing form: \n" + jsonOriginal.toString(1));
            throw e;
        }

        return form;
    }

}

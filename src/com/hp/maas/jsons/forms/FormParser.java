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

        public void put(String key, String value){
            if (org.keySet().contains(key) && value == null){
               org.remove(key);
            }else{
                org.put(key, value);
            }
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


    public static void removeField(JSONObject jsonOriginal , String modelAttribute){
        try {
            GracefulJson json = new GracefulJson(jsonOriginal);


            JSONArray jsonSections = json.getJSONArray(FormConst.FORM.SECTIONS);

            for (int i = 0; i < jsonSections.length(); i++) {
                GracefulJson jsonSection = new GracefulJson(jsonSections.getJSONObject(i));

                JSONArray jsonFields = jsonSection.getJSONArray(FormConst.SECTION.FIELDS);

                Integer toRemove = null;

                for (int j = 0; j < jsonFields.length(); j++) {
                    GracefulJson jsonField = new GracefulJson(jsonFields.getJSONObject(j));

                    String att = jsonField.getString(FormConst.FIELD.MODEL_ATTRIBUTE);

                    if (modelAttribute.equals(att)){
                        toRemove = j;
                        break;
                    }
                }

                if (toRemove != null){
                    jsonFields.remove(toRemove);
                }

            }

        } catch (RuntimeException e) {
            Log.error("Error parsing form: \n" + jsonOriginal.toString(1));
            throw e;
        }
    }

    public static void removeSection(JSONObject jsonOriginal , FormSection sectionToRemove){
        try {
            GracefulJson json = new GracefulJson(jsonOriginal);


            JSONArray jsonSections = json.getJSONArray(FormConst.FORM.SECTIONS);

            Integer toRemove = null;

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

                if (sectionToRemove.equals(section)){
                    toRemove = i;
                }

            }

            if (toRemove != null){
                jsonSections.remove(toRemove);
            }



        } catch (RuntimeException e) {
            Log.error("Error parsing form: \n" + jsonOriginal.toString(1));
            throw e;
        }
    }

    public static void updateLabelsSection(JSONObject jsonOriginal, FormSection sectionDetails, String resourceKey, String name) {
        try {
            GracefulJson json = new GracefulJson(jsonOriginal);


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

                if (sectionDetails.equals(section)){


                    jsonSection.put(FormConst.SECTION.HEADER,null);
                    jsonSection.put(FormConst.SECTION.DOMAIN,null);
                    jsonSection.put(FormConst.SECTION.LOCALIZED_LABEL,null);
                    jsonSection.put(FormConst.SECTION.NAME,name);
                    jsonSection.put(FormConst.SECTION.RESOURCE_KEY,resourceKey);

                    break;
                }

            }


        } catch (RuntimeException e) {
            Log.error("Error parsing form: \n" + jsonOriginal.toString(1));
            throw e;
        }
    }

}

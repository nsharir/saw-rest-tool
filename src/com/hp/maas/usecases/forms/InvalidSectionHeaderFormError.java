package com.hp.maas.usecases.forms;

import com.hp.maas.apis.ResourceBundleAPI;
import com.hp.maas.jsons.forms.Form;
import com.hp.maas.jsons.forms.FormSection;

/**
 * Created by sharir on 08/12/2014.
 */
public class InvalidSectionHeaderFormError implements FormError {

    private Form form;
    private FormSection section;

    public InvalidSectionHeaderFormError(Form form, FormSection section) {
        this.form = form;
        this.section = section;
    }

    @Override
    public Form getForm() {
        return form;
    }

    @Override
    public void fix(FormsTransaction tx) {

        String resourceKey = form.getEntityType()+"."+form.getName().replaceAll(" ","")+"."+toUpperCamelCase(section.getHeader())+".customization";
        tx.createRB(resourceKey,section.getHeader());


        String name = section.getName();

        if (name == null){
            name = toUpperCamelCase(section.getHeader());
        }
        form.updateSectionLabels(section,resourceKey,name);

        tx.update(form);
    }

    @Override
    public String getErrorMessage() {
        return "BAD_HEADER: " +" [entityType=" + form.getEntityType() + "]"+"[form=" + form.getName() + "][section=" + (section.getHeader() != null ? section.getHeader() : section.getName()) + "] section has a bad header. "+getDetailsOfSection();
    }

    private String getDetailsOfSection() {
        return "{" +
                    "name='" + section.getName() + '\'' +
                    ", header='" + section.getHeader() + '\'' +
                    ", resourceKey='" + section.getResourceKey() + '\'' +
                    ", domain='" + section.getDomain() + '\''+
                    "}";
    }

    @Override
    public String getFixMessage() {
         return "Fix - BAD_HEADER: " +" [entityType=" + form.getEntityType() + "]"+"[form=" + form.getName() + "][section=" + (section.getHeader() != null ? section.getHeader() : section.getName()) + "] section updated. "+getDetailsOfSection();
    }


    private  String toUpperCamelCase(String s){
        StringBuilder camel = new StringBuilder();
        String[] words = s.split(" ");
        for (String word : words) {
            String trimmed = word.trim();
            if (!trimmed.isEmpty()){
                camel.append(trimmed.substring(0,1).toUpperCase()).append(trimmed.substring(1));
            }
        }

        return camel.toString();
    }
}

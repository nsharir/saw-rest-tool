package com.hp.maas.usecases.forms;

import com.hp.maas.jsons.forms.Form;
import com.hp.maas.jsons.forms.FormField;
import com.hp.maas.jsons.forms.FormSection;

/**
 * Created by sharir on 08/12/2014.
 */
public class InvalidFieldFormError implements FormError {

    private Form form;
    private FormSection section;
    private FormField field;
    private String error;

    public InvalidFieldFormError(Form form, FormSection section, FormField field, String error) {
        this.form = form;
        this.section = section;
        this.field = field;
        this.error = error;
    }

    @Override
    public Form getForm() {
        return form;
    }

    @Override
    public void fix(FormsTransaction tx) {
        form.removeField(section,field);
        tx.update(form);
    }

    @Override
    public String getErrorMessage() {
        return error+"[form="+form.getName()+"][entity type="+form.getEntityType()+"] [field="+field.getModelAttribute()+"] is invalid";
    }

    @Override
    public String getFixMessage() {
        return "Fix - [form="+form.getName()+"][entity type="+form.getEntityType()+"] [field="+field.getModelAttribute()+"] was removed from form";
    }
}

package com.hp.maas.usecases.forms;

import com.hp.maas.jsons.forms.Form;

/**
 * Created by sharir on 08/12/2014.
 */
public class InvalidEntityTypeFormError implements FormError {

    private Form form;

    public InvalidEntityTypeFormError(Form form) {
        this.form = form;
    }

    @Override
    public Form getForm() {
        return form;
    }

    @Override
    public void fix(FormsTransaction tx) {
        tx.delete(form);
    }

    @Override
    public String getErrorMessage() {
        return "[form="+form.getName()+"][entity type="+form.getEntityType()+"] has no valid entity type";
    }

    @Override
    public String getFixMessage() {
        return "Fix - [form="+form.getName()+"][entity type="+form.getEntityType()+"] was deleted";
    }
}

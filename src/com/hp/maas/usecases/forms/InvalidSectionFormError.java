package com.hp.maas.usecases.forms;

import com.hp.maas.jsons.forms.Form;
import com.hp.maas.jsons.forms.FormSection;

/**
 * Created by sharir on 08/12/2014.
 */
public class InvalidSectionFormError implements FormError {

    private Form form;
    private FormSection section;

    public InvalidSectionFormError(Form form, FormSection section) {
        this.form = form;
        this.section = section;
    }

    @Override
    public Form getForm() {
        return form;
    }

    @Override
    public void fix(FormsTransaction tx) {
        form.removeSection(section);
        tx.update(form);
    }

    @Override
    public String getErrorMessage() {
        return "NO_SECTION: "+" [entityType=" + form.getEntityType() + "]"+"[form=" + form.getName() + "][section="+section.getName()+"] Section has no fields (or all of his fields are invalid)";
    }

    @Override
    public String getFixMessage() {
        return "Fix - NO_SECTION: "+" [entityType=" + form.getEntityType() + "]"+"[form=" + form.getName() + "][section="+section.getName()+"] Section has no fields and was removed";
    }
}

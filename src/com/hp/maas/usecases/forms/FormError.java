package com.hp.maas.usecases.forms;

import com.hp.maas.jsons.forms.Form;

/**
 * Created by sharir on 08/12/2014.
 */
public interface FormError  {

    public Form getForm();

    public void fix(FormsTransaction tx);

    public String getErrorMessage();

    public String getFixMessage();
}

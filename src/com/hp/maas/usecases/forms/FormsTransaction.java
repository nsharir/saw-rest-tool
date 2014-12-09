package com.hp.maas.usecases.forms;

import com.hp.maas.jsons.forms.Form;

/**
 * Created by sharir on 08/12/2014.
 */
public interface FormsTransaction {

    public void delete(Form form);

    public void update(Form form);

    public void commit();

    void createRB(String resourceKey, String header);
}

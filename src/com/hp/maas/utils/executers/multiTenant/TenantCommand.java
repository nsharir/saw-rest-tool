package com.hp.maas.utils.executers.multiTenant;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.utils.executers.reporters.Reporter;

import java.io.IOException;

/**
 * Created by sharir on 06/01/2015.
 */
public interface TenantCommand{

    public void execute(Server server, Tenant tenant , Reporter reporter);

}

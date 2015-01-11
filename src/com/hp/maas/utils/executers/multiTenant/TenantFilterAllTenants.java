package com.hp.maas.utils.executers.multiTenant;

import com.hp.maas.apis.model.tenatManagment.Tenant;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by sharir on 06/01/2015.
 */
public class TenantFilterAllTenants implements TenantFilter {



    public TenantFilterAllTenants() {

    }

    public boolean shouldRun(Tenant t){
        return true;
    }
}

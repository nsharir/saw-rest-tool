package com.hp.maas.utils.executers.multiTenant;

import com.hp.maas.apis.model.tenatManagment.Tenant;

/**
 * Created by sharir on 06/01/2015.
 */
public interface TenantFilter {
    public boolean shouldRun(Tenant t);
}

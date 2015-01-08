package com.hp.maas.utils.executers.multiTenant;

import com.hp.maas.apis.model.tenatManagment.Tenant;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by sharir on 06/01/2015.
 */
public class TenantFilterByIds implements TenantFilter {

    Set<String> ids = new HashSet<String>();


    public TenantFilterByIds(String ... tenantIds) {
        for (String tenantId : tenantIds) {
            ids.add(tenantId);
        }
    }

    public boolean shouldRun(Tenant t){
        return ids.contains(t.getId());
    }
}

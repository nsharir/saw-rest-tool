package com.hp.maas.usecases.SLA;

import com.hp.maas.apis.EntityReaderAPI;
import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.entity.EntityInstance;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.apis.model.query.SimpleFilterElement;
import com.hp.maas.apis.model.tenatManagment.Tenant;
import com.hp.maas.utils.executers.multiTenant.TenantCommand;
import com.hp.maas.utils.executers.reporters.LogLevel;
import com.hp.maas.utils.executers.reporters.Reporter;

import java.util.List;

/**
 * Created by sharir on 19/02/2015.
 */
public class FindDefaultSLAs implements TenantCommand{
    @Override
    public void execute(Server server, Tenant tenant, Reporter reporter) {
        EntityReaderAPI reader = server.getEntityReaderAPI();
        List<EntityInstance> entityInstances = reader.readFullLayout("Agreement", new FilterBuilder(new SimpleFilterElement("DefaultSLA", "=", true)).and(new SimpleFilterElement("PhaseId", "=", "active")));
        if (entityInstances.size() > 1){
            reporter.report(LogLevel.INFO,"[TO_MANY_DEFAULTS] found:" );
            for (EntityInstance entityInstance : entityInstances) {
                reporter.report(LogLevel.INFO,entityInstance.toString());
            }
        }
    }
}

package com.hp.maas.apis.model.devToProd.sync;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.entity.EntityInstance;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/14/14
 * Time: 10:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class SyncStepUpdate implements SyncStep {

    private List<EntityInstance> entitiesToUpdate;
    private String stepInfo;
    private Server targetServer;


    public SyncStepUpdate(List<EntityInstance> entitiesToUpdate, String stepInfo, Server targetServer) {
        this.entitiesToUpdate = entitiesToUpdate;
        this.stepInfo = stepInfo;
        this.targetServer = targetServer;
    }

    @Override
    public String describe() {
        return "Update "+entitiesToUpdate.size()+ " records - "+stepInfo;
    }

    @Override
    public String detailedLog() {
        StringBuilder str = new StringBuilder();
        str.append(describe()).append("\n");
        str.append("--------------------------------------");

        for (EntityInstance instance : entitiesToUpdate) {
            str.append(instance);
        }
        str.append("--------------------------------------");
        return str.toString();
    }

    @Override
    public void execute() {
        targetServer.getEntityWriterAPI().updateEntities(entitiesToUpdate);
    }
}

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
public class SyncStepInsert implements SyncStep {

    private List<EntityInstance> entitiesToInsert;
    private String stepInfo;
    private Server targetServer;


    public SyncStepInsert(List<EntityInstance> entitiesToInsert, String stepInfo, Server targetServer) {
        this.entitiesToInsert = entitiesToInsert;
        this.stepInfo = stepInfo;
        this.targetServer = targetServer;
    }

    @Override
    public String describe() {
        return "Create "+entitiesToInsert.size()+ " records - "+stepInfo;
    }

    @Override
    public String detailedLog() {
        StringBuilder str = new StringBuilder();
        str.append(describe()).append("\n");
        str.append("--------------------------------------");

        for (EntityInstance instance : entitiesToInsert) {
            str.append(instance);
        }
        str.append("--------------------------------------");
        return str.toString();
    }

    @Override
    public void execute() {
        targetServer.getEntityWriterAPI().insertEntities(entitiesToInsert);
    }
}

package com.hp.maas.usecases.taskPlan;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by sharir on 27/05/2015.
 */
public class TaskPlanTaskNode extends TaskPlanNode {

    private JSONObject taskProperties;
    private JSONArray businessRules;

    public TaskPlanTaskNode(String id, JSONObject taskProperties, JSONArray businessRules) {
        super(id, TaskPlanNodeType.Task);
        this.taskProperties = taskProperties;
        this.businessRules = businessRules;
    }

    @Override
    protected void enrichJson(JSONObject json) {
        json.put("TaskProperties",this.taskProperties);
        json.put("BusinessRules",this.businessRules);
    }
}

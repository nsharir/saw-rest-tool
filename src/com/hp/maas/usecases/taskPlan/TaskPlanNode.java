package com.hp.maas.usecases.taskPlan;

import org.json.JSONObject;

import javax.lang.model.type.NoType;

/**
 * Created by sharir on 27/05/2015.
 */
public abstract class TaskPlanNode {

    private String id;
    private TaskPlanNodeType type;

    protected TaskPlanNode(String id, TaskPlanNodeType type) {
        this.id = id;
        this.type = type;
    }

    public TaskPlanNodeType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        json.put("Id",this.id);
        json.put("NodeType",this.type.name());
        enrichJson(json);
        return json;
    }

    protected  void enrichJson(JSONObject json){

    }
}

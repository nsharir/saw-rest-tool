package com.hp.maas.usecases.taskPlan;

import org.json.JSONObject;

/**
 * Created by sharir on 27/05/2015.
 */
public class TaskPlanTransition {
    private String id;
    private String from;
    private String to;
    private String switchConditionValue;

    public TaskPlanTransition(String from, String to, String switchConditionValue) {
        this.id = "transition_from_"+from+"_to_"+to;
        this.from = from;
        this.to = to;
        this.switchConditionValue = switchConditionValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSwitchConditionValue() {
        return switchConditionValue;
    }

    public void setSwitchConditionValue(String switchConditionValue) {
        switchConditionValue = switchConditionValue;
    }

    public JSONObject toJson(){

        JSONObject json = new JSONObject();
        TaskPlanTransition transition = this;
        json.put("Id", transition.id);
        json.put("FromNodeId", transition.from);
        json.put("ToNodeId", transition.to);
        if (transition.switchConditionValue != null) {
            json.put("SwitchConditionValue", transition.switchConditionValue);
        }

        return json;
    }

}

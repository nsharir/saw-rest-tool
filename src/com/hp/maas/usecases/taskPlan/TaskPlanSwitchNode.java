package com.hp.maas.usecases.taskPlan;

import org.json.JSONObject;

/**
 * Created by sharir on 27/05/2015.
 */
public class TaskPlanSwitchNode extends TaskPlanNode {

    private boolean isBoolean;
    private String  switchOn;
    private String displayLabel;

    public TaskPlanSwitchNode(String id,String displayLabel, String switchOn,boolean isBoolean) {
        super(id, TaskPlanNodeType.Switch);
        this.isBoolean = isBoolean;
        this.switchOn = switchOn;
        this.displayLabel = displayLabel;
    }

    @Override
    protected void enrichJson(JSONObject json) {
        json.put("IsBoolean",this.isBoolean);
        json.put("SwitchOn",this.switchOn);
        json.put("DisplayLabelKey",this.displayLabel);
    }
}

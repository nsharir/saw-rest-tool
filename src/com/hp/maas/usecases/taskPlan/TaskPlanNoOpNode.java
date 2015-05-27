package com.hp.maas.usecases.taskPlan;

/**
 * Created by sharir on 27/05/2015.
 */
public class TaskPlanNoOpNode extends TaskPlanNode {

    public TaskPlanNoOpNode(String id) {
        super(id, TaskPlanNodeType.NoOp);
    }
}

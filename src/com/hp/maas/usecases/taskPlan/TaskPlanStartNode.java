package com.hp.maas.usecases.taskPlan;

/**
 * Created by sharir on 27/05/2015.
 */
public class TaskPlanStartNode extends TaskPlanNode {

    public TaskPlanStartNode(String id) {
        super(id, TaskPlanNodeType.Start);
    }
}

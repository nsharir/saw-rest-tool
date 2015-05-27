package com.hp.maas.usecases.taskPlan;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.entity.EntityInstance;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.apis.model.query.IdsFilterElement;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by sharir on 27/05/2015.
 */
public class TaskPlanUtils {



    private static class Env {

        private Env(String userOption, String label, String id) {
            this.userOption = userOption;
            this.label = label;
            this.id = id;
        }

        String userOption;
        String label;
        String id;
    }

    public static void updateTaskPanForCICD(Server server){

        List<EntityInstance> offering = server.getEntityReaderAPI().readFullLayout("Offering", new FilterBuilder(new IdsFilterElement("10404")));

        EntityInstance record = offering.get(0);

        record.setFieldValue("TaskPlanForFulfill",TaskPlanUtils.getTaskPlanForCICDOffering().toString(1));

        server.getEntityWriterAPI().updateEntity(record);

    }

    public static JSONObject getTaskPlanForCICDOffering(){

        final String START_NODE = "StartNode";

        List<TaskPlanNode> nodes = new ArrayList<TaskPlanNode>();
        List<TaskPlanTransition> transitions = new ArrayList<TaskPlanTransition>();


        //============================================================
        List<Env> envs = new ArrayList<Env>();

        envs.add(new Env("DevlabCIInfra_c","Devlab - CI Infra","10019"));
        envs.add(new Env("DevlabDevInfra_c","Devlab - Dev Infra","10018"));
        envs.add(new Env("Devlab911_c","Devlab - 911","10020"));
        envs.add(new Env("DevlabLatest_c","Devlab - Latest","10023"));
        envs.add(new Env("DevlabLocalization_c","Devlab - Localization","10104"));
        envs.add(new Env("DevlabMirrow_c","Devlab - Mirrow","10103"));
        envs.add(new Env("DevlabNightly_c","Devlab - Nightly","10102"));
        envs.add(new Env("DevlabNightlyQA_c","Devlab - Nightly QA","10021"));
        envs.add(new Env("DevlabPortalInteg_c","Devlab - Portal Integ","10022"));
        envs.add(new Env("DevlabSanDiego1_c","Devlab - SanDiego 1","10024"));
        envs.add(new Env("DevlabSanDiego2_c","Devlab - SanDiego 2","10105"));
        envs.add(new Env("DevlabShanghai_c","Devlab - Shanghai","10106"));
        envs.add(new Env("ProdLondon_c","Prod - London","10033"));
        envs.add(new Env("ProdNGDC_c","Prod - NGDC","10032"));
        envs.add(new Env("ProdNGDC2_c","Prod - NGDC 2","10108"));
        envs.add(new Env("SaaS911_c","SaaS - 911","10028"));
        envs.add(new Env("SaaSJenkins_c","SaaS - Jenkins","10027"));
        envs.add(new Env("SaaSPCOE1_c","SaaS - PCOE1","10029"));
        envs.add(new Env("SaaSPCOE2_c","SaaS - PCOE 2","10030"));
        envs.add(new Env("SaaSQA_c","SaaS - QA","10025"));
        envs.add(new Env("SaaSQA3_c","SaaS - QA 3","10026"));
        envs.add(new Env("SaaSQANoach_c","SaaS - QA (Noach)","10031"));
        envs.add(new Env("SaaSSecurity_c","SaaS - Security","10107"));
        envs.add(new Env("Staging_c","Staging","10109"));
        //============================================================


        TaskPlanStartNode start = new TaskPlanStartNode(START_NODE);
        nodes.add(start);

        Set<TaskPlanNode> sourceNodes = new HashSet<TaskPlanNode>();
        sourceNodes.add(start);


        for (Env env : envs) {

            //create switch condition
            String switchNodeId = "Switch_"+env.userOption;
            String condition = "${entity.UserOptions."+env.userOption+" == true}";
            String label = env.label+"?";
            TaskPlanSwitchNode switchNode = new TaskPlanSwitchNode(switchNodeId, label, condition,true);
            nodes.add(switchNode);



            //create taskNode
            String taskNodeId = "Task_node_"+env.userOption;
            JSONObject taskProp = new JSONObject();
            taskProp.put("contextType","Request");
            //taskProp.put("contextId",null);
            taskProp.put("DisplayLabelKey","Create change for "+env.label);
            taskProp.put("ParentEntityType","Request");
            taskProp.put("PlatformTaskType","AutomaticTask");




            JSONArray entityMapping = new JSONArray();

            //=======================================================================================
            Map<String,String> fieldToValue = new HashMap<String, String>();
            fieldToValue.put("DisplayLabel","${concat(entity.UserOptions.ChangeTitle_c,'   [','"+env.label+"',']')}");
            fieldToValue.put("Justification","${entity.UserOptions.ChangeJustification_c}");
            fieldToValue.put("Description","${entity.UserOptions.ChangeDescription_c}");
            fieldToValue.put("OwnedByPerson","${entity.RequestedByPerson}");
            fieldToValue.put("NeedToUpdateLookup_c","${entity.UserOptions.RequieresLookupChange_c}");
            fieldToValue.put("AffectsActualService",env.id);
            fieldToValue.put("RequiresInfraChange_c","${entity.UserOptions.RequiresInfraChange_c}");
            fieldToValue.put("RndETA_c","${entity.UserOptions.RnDETA_c}");
            fieldToValue.put("ChangeWorkflowType","NormalChange");
            fieldToValue.put("BasedOnChangeModel","10316");
            //=======================================================================================

            for (Map.Entry<String, String> entry : fieldToValue.entrySet()) {
                JSONObject field = new JSONObject();
                field.put("Name",entry.getKey());
                field.put("Value",entry.getValue());
                entityMapping.put(field);
            }

            JSONArray brParameters = new JSONArray();

            JSONObject associationParameter = new JSONObject();
            associationParameter.put("Name","associationType");
            associationParameter.put("Value","ChangeCausedByRequest");

            JSONObject entityValuesParameter = new JSONObject();
            entityValuesParameter.put("Name","entityValues");
            entityValuesParameter.put("Values",entityMapping);



            brParameters.put(associationParameter);
            brParameters.put(entityValuesParameter);

            JSONArray brs = new JSONArray();
            JSONObject br = new JSONObject();
            brs.put(br);

            br.put("Type","action");
            br.put("TemplateId","createEntityAndAssociation");
            br.put("Parameters",brParameters);

            TaskPlanTaskNode taskNode = new TaskPlanTaskNode(taskNodeId,taskProp,brs);


            nodes.add(taskNode);

            transitions.add(new TaskPlanTransition(switchNodeId,taskNodeId,"${true}"));


            //create transition to switch
            for (TaskPlanNode sourceNode : sourceNodes) {

                String conditionValue = null;
                if (sourceNode.getType().equals(TaskPlanNodeType.Switch)){
                    conditionValue = "${false}";
                }

                transitions.add(new TaskPlanTransition(sourceNode.getId(), switchNodeId, conditionValue));
            }

            sourceNodes.clear();

            sourceNodes.add(switchNode);
            sourceNodes.add(taskNode);


        }


        TaskPlanNoOpNode endNoOpNode = new TaskPlanNoOpNode("EndNoOpNode");
        nodes.add(endNoOpNode);
        for (TaskPlanNode sourceNode : sourceNodes) {

            String conditionValue = null;
            if (sourceNode.getType().equals(TaskPlanNodeType.Switch)){
                conditionValue = "${false}";
            }

            transitions.add(new TaskPlanTransition(sourceNode.getId(),endNoOpNode.getId(), conditionValue));
        }



        JSONObject plan = new JSONObject();

        JSONArray transitionsArray = new JSONArray();
        JSONArray nodesArray = new JSONArray();

        for (TaskPlanTransition transition : transitions) {
            transitionsArray.put(transition.toJson());
        }

        for (TaskPlanNode node : nodes) {
            nodesArray.put(node.toJson());
        }

        plan.put("Transitions",transitionsArray);
        plan.put("Nodes",nodesArray);


        return plan;
    }
}

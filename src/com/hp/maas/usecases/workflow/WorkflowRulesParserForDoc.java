package com.hp.maas.usecases.workflow;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by sharir on 02/12/2014.
 */
public class WorkflowRulesParserForDoc {

    public static final String EMPTY_VALUE = "-";

    public void run() {


        List<WorkflowRulePath> records = new ArrayList<WorkflowRulePath>();

        String path = System.getProperty("workflow-file");
        if (path == null || "".equals(path)) {
            throw new RuntimeException("Please define the path to the workflow file. (workflow-file)");
        }

        File confFile = new File(path);

        if (!confFile.exists()) {
            throw new RuntimeException("No such file " + path);
        }
        String workflowStr;

        try {
            workflowStr = FileUtils.readFileToString(confFile);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file " + path, e);
        }
        workflowStr = workflowStr.replaceAll("\n", " ");

        JSONObject workflow = new JSONObject(workflowStr);

        JSONArray ruleDefinitions = workflow.getJSONArray("RuleDefinitions");

        Map<String, JSONObject> rulesMap = new HashMap<String, JSONObject>();

        for (int i = 0; i < ruleDefinitions.length(); i++) {
            JSONObject ruleDef = ruleDefinitions.getJSONObject(i);
            rulesMap.put(ruleDef.getString("id"), ruleDef);
        }

        //---------------------------

        appendEvents(records, workflow,"EventHandlers", EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE);

        JSONArray processes = workflow.getJSONArray("Processes");
        for (int i = 0; i < processes.length(); i++) {
            JSONObject processJson = processes.getJSONObject(i);
            String process = processJson.getString("id");
            appendEvents(records, processJson,"EventHandlers", process, EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE);

            JSONArray metaphases = processJson.getJSONArray("Metaphases");
            for (int j = 0; j < metaphases.length(); j++) {
                JSONObject metaphaseJson = metaphases.getJSONObject(j);
                String metaphase = metaphaseJson.getString("id");
                appendEvents(records, metaphaseJson,"EventHandlers", process, metaphase, EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE);
            }

            JSONArray phases = processJson.getJSONArray("Phases");
            for (int j = 0; j < phases.length(); j++) {
                JSONObject phaseJson = phases.getJSONObject(j);
                String phase = phaseJson.getString("id");
                appendEvents(records, phaseJson,"EventHandlers", process, phaseJson.getString("MetaphaseId"), phase, EMPTY_VALUE, EMPTY_VALUE, EMPTY_VALUE);
            }

            JSONArray transitions = processJson.getJSONArray("Transitions");
            for (int j = 0; j < transitions.length(); j++) {
                JSONObject transitionJson = transitions.getJSONObject(j);
                String transition = transitionJson.getString("id");
                appendEvents(records, transitionJson,"OnTransitionRules", process, EMPTY_VALUE, EMPTY_VALUE, transition, transitionJson.getString("FromPhaseId"), transitionJson.getString("ToPhaseId"));

            }
        }

        dumpCSV(workflow.getString("EntityType"), records, rulesMap);


    }

    private void appendEvents(List<WorkflowRulePath> records, JSONObject container,String property, String process, String metaphase, String phase, String transition, String phaseEnd1, String phaseEnd2) {
        if (!container.keySet().contains(property)) {
            return;
        }
        JSONObject eventHandlersContainer = container.getJSONObject(property);


        String[] eventHandlerKey = new String[]{"EventHandlers", "DevPreEventHandlers", "DevPostEventHandlers"};

        for (String handler : eventHandlerKey) {

            if (!eventHandlersContainer.keySet().contains(handler)) {
                continue;
            }
            JSONObject eventHandlers = eventHandlersContainer.getJSONObject(handler);

            List<String> events = new ArrayList<String>(eventHandlers.keySet());

            for (int i = 0; i < events.size(); i++) {
                String key = events.get(i);
                JSONArray rulesList = eventHandlers.getJSONArray(key);

                for (int j = 0; j < rulesList.length(); j++) {
                    JSONObject ruleJson = rulesList.getJSONObject(j);

                    WorkflowRulePath rule = new WorkflowRulePath();
                    rule.event = key;
                    rule.ruleId = ruleJson.getString("RuleId");
                    if (ruleJson.keySet().contains("ConditionId")) {
                        rule.conditionId = ruleJson.getString("ConditionId");
                    }
                    rule.type = ruleJson.getString("RuleType");

                    rule.phaseEnd1 = phaseEnd1;
                    rule.phaseEnd2 = phaseEnd2;
                    rule.transition = transition;
                    rule.phase = phase;
                    rule.metaphase = metaphase;
                    rule.process = process;

                    records.add(rule);
                }

            }

        }


    }

    private void dumpCSV(String entityType, List<WorkflowRulePath> records, Map<String, JSONObject> rulesMap) {
        StringBuilder csv = new StringBuilder();
        CSVPrinter printer;
        try {
            printer = new CSVPrinter(csv, CSVFormat.EXCEL.withRecordSeparator("\n").withDelimiter(','));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            printer.printRecord("RuleId",
                    "RuleType",
                    "TemplateId",
                    "Process",
                    "Metaphase",
                    "Phase",
                    "Transition",
                    "end1",
                    "end2",
                    "Parameters",
                    "helpKey",
                    "helpText");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        for (WorkflowRulePath record : records) {
            JSONObject ruleJson = rulesMap.get(record.ruleId);
            try {
                printer.printRecord(record.ruleId,
                        record.type,
                        ruleJson.getString("TemplateId"),
                        record.process,
                        record.metaphase,
                        record.phase,
                        record.transition,
                        record.phaseEnd1,
                        record.phaseEnd2,
                        ruleJson.getJSONArray("Parameters").toString(),
                        "workflow."+entityType+".rule." + record.ruleId + ".helpText",
                        EMPTY_VALUE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(csv);
    }

}

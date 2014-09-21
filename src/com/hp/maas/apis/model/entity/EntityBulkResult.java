package com.hp.maas.apis.model.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sharir
 * Date: 12/09/14
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
public class EntityBulkResult {
    private Map<String,BulkResultEntry> resultsMap = new HashMap<String, BulkResultEntry>();
    private List<BulkResultEntry> failedList = new ArrayList<BulkResultEntry>();
    private List<BulkResultEntry> succeedList = new ArrayList<BulkResultEntry>();
    private List<BulkResultEntry> results;


    EntityBulkResult(List<BulkResultEntry> results) {
        this.results = results;
        for (BulkResultEntry result : results) {
            resultsMap.put(result.getId(),result);
            if (result.getCompletionStatus() == CompletionStatus.OK){
                succeedList.add(result);
            }else{
                failedList.add(result);
            }
        }
    }

    public boolean isFailed(){
        return  !failedList.isEmpty();
    }

    public Map<String, BulkResultEntry> getResultsMap() {
        return resultsMap;
    }

    public List<BulkResultEntry> getFailures() {
        return failedList;
    }

    public List<BulkResultEntry> getSucceeded() {
        return succeedList;
    }

    public List<BulkResultEntry> getAll(){
      return results;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Bulk:[ ").append(" Failed: ").append(getFailures().size()).append(", ").append(" Succeeded: ").append(getSucceeded().size()).append(", Results:\n");
        for (String key : resultsMap.keySet()) {
            str.append("     ").append(resultsMap.get(key)).append("\n");
        }
        str.append("]");
        return str.toString();
    }
}

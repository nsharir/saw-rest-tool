package com.hp.maas.apis.model.entity;

import com.hp.maas.apis.Server;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sharir
 * Date: 12/09/14
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 */
public class BulkResultsParser {

    private static final String ENTITY_RESULT_LIST = "entity_result_list";
    private static final String ENTITY = "entity";
    private static final String COMPLETION_STATUS = "completion_status";
    public static final String ERROR_DETAILS = "errorDetails";
    public static final String MESSAGE = "message";

    public static EntityBulkResult toEntityBulkResult(String responseJson, Server server){

        JSONObject json = new JSONObject(responseJson);
        JSONArray resultsJsonArray = json.getJSONArray(ENTITY_RESULT_LIST);
        List<BulkResultEntry> entries  = new ArrayList<BulkResultEntry>();

        for (int i=0;i<resultsJsonArray.length();i++){
            JSONObject entryJson = resultsJsonArray.getJSONObject(i);
            EntityInstance instance = EntityInstanceParser.entityInstanceFromJson(entryJson.getJSONObject(ENTITY).toString(),server);
            String status = entryJson.getString(COMPLETION_STATUS);
            CompletionStatus completionStatus;
            String message = "";
            if (CompletionStatus.OK.name().equals(status)){
                completionStatus = CompletionStatus.OK;
                message = "Ok";
            }else{
                completionStatus = CompletionStatus.FAILED;
                message = entryJson.getJSONObject(ERROR_DETAILS).getString(MESSAGE);
            }

            entries.add(new BulkResultEntry(instance, completionStatus, message));
        }

        return new EntityBulkResult(entries);

    }


}

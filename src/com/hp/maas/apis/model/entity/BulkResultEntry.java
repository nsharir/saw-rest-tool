package com.hp.maas.apis.model.entity;

/**
 * Created with IntelliJ IDEA.
 * User: sharir
 * Date: 12/09/14
 * Time: 14:06
 * To change this template use File | Settings | File Templates.
 */
public class BulkResultEntry {

    private String id;
    private CompletionStatus completionStatus;
    private String message;
    private EntityInstance entity;

     BulkResultEntry(EntityInstance entity , CompletionStatus completionStatus, String message) {
        this.id = entity.getFieldValue("Id").toString();
        this.completionStatus = completionStatus;
        this.message = message;
        this.entity = entity;
    }

    public String getId() {
        return id;
    }

    public CompletionStatus getCompletionStatus() {
        return completionStatus;
    }

    public String getMessage() {
        return message;
    }

    public EntityInstance getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        return "{"+completionStatus+" ,"+" id:"+id+" , "+" message: '"+message+"'}";
    }
}

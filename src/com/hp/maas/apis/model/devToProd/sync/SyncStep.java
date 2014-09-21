package com.hp.maas.apis.model.devToProd.sync;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/14/14
 * Time: 10:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SyncStep {
    public String describe();

    public String detailedLog();

    public void execute();


}

package com.hp.maas.apis.model.devToProd.sync;

import com.hp.maas.apis.model.entity.EntityInstance;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/14/14
 * Time: 11:42 PM
 * To change this template use File | Settings | File Templates.
 */
class LoopEntityWrapper {
    public EntityInstance original;
    public EntityInstance clone;

    LoopEntityWrapper(EntityInstance original, EntityInstance clone) {
        this.original = original;
        this.clone = clone;
    }
}

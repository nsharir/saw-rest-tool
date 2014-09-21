package com.hp.maas.tools;

import com.hp.maas.apis.model.entity.EntityInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/16/14
 * Time: 10:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class DevProdCompareResult {

    List<EntityInstance> existInDevNotInProd;
    List<EntityInstance> existInProdNotInDev;

    public DevProdCompareResult(List<EntityInstance> existInDevNotInProd, List<EntityInstance> existInProdNotInDev) {
        this.existInDevNotInProd = existInDevNotInProd;
        this.existInProdNotInDev = existInProdNotInDev;
    }

    public List<EntityInstance> getExistInDevNotInProd() {
        return existInDevNotInProd;
    }

    public List<EntityInstance> getExistInProdNotInDev() {
        return existInProdNotInDev;
    }
}

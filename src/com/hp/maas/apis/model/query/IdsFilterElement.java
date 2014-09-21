package com.hp.maas.apis.model.query;

import com.hp.maas.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/13/14
 * Time: 7:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class IdsFilterElement implements FilterElement {

    private Object[] ids;

    public IdsFilterElement(Object... ids) {
        this.ids = ids;
    }

    @Override
    public String getFilterSyntax() {
        return "Id in ("+ StringUtils.toCommaSeparatedString(Arrays.asList(ids), false)+")";
    }

    @Override
    public String toString() {
        return getFilterSyntax();
    }
}

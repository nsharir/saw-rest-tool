package com.hp.maas.apis.model.query;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/13/14
 * Time: 7:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class APISyntaxFilterElement implements FilterElement {
    private String filter;

    public APISyntaxFilterElement(String filter) {
        this.filter = filter;
    }

    @Override
    public String getFilterSyntax() {
        return filter;
    }

    @Override
    public String toString() {
        return getFilterSyntax();
    }
}

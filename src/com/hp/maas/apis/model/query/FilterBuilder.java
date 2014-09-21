package com.hp.maas.apis.model.query;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/13/14
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class FilterBuilder  {

    private StringBuilder query = new StringBuilder();

    public FilterBuilder(FilterElement filterElement) {
        query.append(filterElement.getFilterSyntax());
    }

    public FilterBuilder and(FilterElement filterElement){
        query.append(" and ").append(filterElement.getFilterSyntax());
        return this;
    }

    public String getFilterString (){
        try {
            return URLEncoder.encode(query.toString(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
           throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return getFilterString();
    }
}

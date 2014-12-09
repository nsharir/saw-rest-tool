package com.hp.maas.apis.model.query;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/13/14
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleFilterElement implements FilterElement {
    private String left;
    private String operator;
    private Object right;


    public SimpleFilterElement(String left, String operator, Object right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String getFilterSyntax() {
        String stringValue = "";
        if (right != null && String.class.equals(right.getClass())){
            stringValue = "'";
        }
        return left+" "+operator+" "+stringValue+right+stringValue+" ";
    }
}

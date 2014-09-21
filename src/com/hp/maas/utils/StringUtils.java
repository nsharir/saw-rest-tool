package com.hp.maas.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 10:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class StringUtils {
    public static String toCommaSeparatedString(String[] strings) {
        List<String> list = Arrays.asList(strings);
        return toCommaSeparatedString(list,false);
    }

    public static String toCommaSeparatedString(List strings, boolean isString) {
        StringBuilder layoutStr = new StringBuilder();

        for (int i=0;i<strings.size();i++) {
            if (isString){
                layoutStr.append("'").append(strings.get(i)).append("'");
            }else{
                layoutStr.append(strings.get(i));
            }
            if (i< strings.size()-1){
                layoutStr.append(",");
            }
        }
        return layoutStr.toString();
    }

}

package com.hp.maas.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 10:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class Log {

    private static boolean logEnabled = false;

    public static void log(String str){
        if (!logEnabled) return;

        System.out.println(str);
    }

    public static void info(String str){
        if (!logEnabled) return;
        System.out.println(str);
    }

    public static void error(String str){
        System.out.println("ERROR!! - "+str);
    }
}

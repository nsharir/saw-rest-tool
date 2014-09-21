package com.hp.maas.configuration;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/13/14
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class Configuration {
    private static IdentificationRules identificationRules;
    private static List<String> typesToPull;
    public static void load(){
        identificationRules = ConfigurationParser.loadIdentificationRules();
        typesToPull = ConfigurationParser.getTypesToPull();
    }

    public static IdentificationRules getIdentificationRules() {
        return identificationRules;
    }

    public static List<String> getTypesToPull() {
        return typesToPull;
    }
}

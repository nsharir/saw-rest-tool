package com.hp.maas.configuration;

import com.hp.maas.apis.model.metadata.MetadataParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/13/14
 * Time: 12:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationParser {

    public static final String RULES = "identificationRules";
    public static final String TYPE = "type";
    public static final String IDENTIFICATION_FIELD_NAME = "IdentificationFieldName";
    public static final String TYPES_TO_PULL = "typesToPull";

    private static IdentificationRules identificationRules;
    private static List<String> typesToPull = new ArrayList<String>();

    static {
        init();
    }

    private static void init() {
        InputStream stream  = ConfigurationParser.class.getClassLoader().getResourceAsStream("\\xcontent\\dev.to.prod.conf.json");
        StringWriter str = new StringWriter();
        try {
            IOUtils.copy(stream, str,"UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject json = new JSONObject(str.toString());
        JSONArray arr = json.getJSONArray(RULES);

        Map<String,String> map = new HashMap<String, String>();

        for (int i=0; i<arr.length();i++){
            JSONObject entry = arr.getJSONObject(i);
            map.put(entry.getString(TYPE),entry.getString(IDENTIFICATION_FIELD_NAME));
        }

        identificationRules = new IdentificationRules(map);

        typesToPull.clear();

        JSONArray typesToPullArr = json.getJSONArray(TYPES_TO_PULL);

        for (int i=0; i<typesToPullArr.length();i++){
            typesToPull.add(typesToPullArr.getString(i));
        }

    }

    public static IdentificationRules loadIdentificationRules(){
       return identificationRules;
    }

    public static List<String> getTypesToPull() {
        return typesToPull;
    }
}

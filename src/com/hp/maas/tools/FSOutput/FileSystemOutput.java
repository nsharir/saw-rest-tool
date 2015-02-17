package com.hp.maas.tools.FSOutput;

import com.hp.maas.utils.StringUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sharir on 22/01/2015.
 */
public class FileSystemOutput {

    private String basePath;
    private String identifier;

    private File outputFolder;

    Map<String,File> contextMap = new HashMap<String, File>();

    public FileSystemOutput(String basePath) {

        this.basePath = basePath;

        outputFolder = new File(basePath);

        if (!outputFolder.exists()){
            throw new RuntimeException("No such output path "+basePath);
        }

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy__HH-mm-ss");
        String executionFolder = outputFolder.getAbsolutePath()+File.separator+format.format(new Date());
        outputFolder = new File(executionFolder);

        outputFolder.mkdirs();
    }

    public void dump (String context, String fileName , String data) throws IOException {
        File folder;
        if (context != null) {
            folder = contextMap.get(context);
            if (folder == null) {
                folder = new File(outputFolder.getAbsolutePath() + File.separator + toUpperCamelCase(context));
                contextMap.put(context, folder);
                folder.mkdirs();
            }
        }else{
            folder = outputFolder.getAbsoluteFile();
        }

        File file = new File(folder.getAbsolutePath()+File.separator+toUpperCamelCase(fileName));

        FileUtils.write(file,data,"UTF-8",true);
    }

    private  String toUpperCamelCase(String s){
        StringBuilder camel = new StringBuilder();
        String[] words = s.split(" ");
        for (String word : words) {
            String trimmed = word.trim();
            if (!trimmed.isEmpty()){
                camel.append(trimmed.substring(0,1).toUpperCase()).append(trimmed.substring(1));
            }
        }

        return camel.toString();
    }
}

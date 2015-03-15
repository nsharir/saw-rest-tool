package com.hp.maas.usecases.SLA;

import com.hp.maas.tools.FSOutput.FileSystemOutput;
import com.hp.maas.utils.executers.reporters.ConsoleReporter;
import com.hp.maas.utils.executers.reporters.LogLevel;
import com.hp.maas.utils.executers.reporters.Reporter;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;

/**
 * Created by sharir on 19/02/2015.
 */
public class FileSystemReporter implements Reporter{
    private Reporter console = new ConsoleReporter(LogLevel.INFO);
    private FileSystemOutput out;
    private String context;
    private String fileName;

    public FileSystemReporter(FileSystemOutput out, String context) {
        this.out = out;
        this.context = context;
        this.fileName = context + ".log";
    }

    public FileSystemReporter(FileSystemOutput out) {
        this.out = out;
        this.fileName = "all" + ".log";
    }

    @Override
    public void report(LogLevel level, String str) {
        console.report(level, str);
        try {

            out.dump(context, fileName,level + ": " + str+"\n");
        } catch (IOException e) {
            console.report(LogLevel.ERROR, ExceptionUtils.getStackTrace(e));
        }
    }
}

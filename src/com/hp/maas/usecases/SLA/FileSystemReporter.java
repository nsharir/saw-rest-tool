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
    private LogLevel level;
    private Reporter console;
    private FileSystemOutput out;
    private String context;
    private String fileName;


    public FileSystemReporter(FileSystemOutput out, String context) {
        this(out,context,LogLevel.INFO);
    }

    public FileSystemReporter(FileSystemOutput out, String context, LogLevel logLevel) {
        this.level = logLevel;
        this.console = new ConsoleReporter(logLevel);
        this.out = out;
        this.context = context;
        this.fileName = context + ".log";
    }

    public FileSystemReporter(FileSystemOutput out) {
        this (out,"all.log");
    }

    @Override
    public void report(LogLevel level, String str) {
        if (level.ordinal()   > level.ordinal()) {
            return;
        }
        console.report(level, str);
        try {

            out.dump(context, fileName,level + ": " + str+"\n");
        } catch (IOException e) {
            console.report(LogLevel.ERROR, ExceptionUtils.getStackTrace(e));
        }
    }
}

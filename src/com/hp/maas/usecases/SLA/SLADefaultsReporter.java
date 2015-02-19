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
public class SLADefaultsReporter implements Reporter{
    private Reporter console = new ConsoleReporter(LogLevel.INFO);
    private FileSystemOutput out;
    private String context;

    public SLADefaultsReporter(String baseFolder, String context) {
        out = new FileSystemOutput(baseFolder);
        this.context = context;
    }

    @Override
    public void report(LogLevel level, String str) {
        console.report(level, str);
        try {
            out.dump(context,context+".log",level + ": " + str+"\n");
        } catch (IOException e) {
            console.report(LogLevel.ERROR, ExceptionUtils.getStackTrace(e));
        }
    }
}

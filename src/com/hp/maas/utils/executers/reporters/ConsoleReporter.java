package com.hp.maas.utils.executers.reporters;

/**
 * Created by sharir on 06/01/2015.
 */
public class ConsoleReporter implements Reporter {

    LogLevel minimumLogLevel = LogLevel.DEBUG;

    public ConsoleReporter() {
    }

    public ConsoleReporter(LogLevel minimumLogLevel) {
        this.minimumLogLevel = minimumLogLevel;
    }

    @Override
    public void report(LogLevel level, String str) {
        if (minimumLogLevel.ordinal()   <= level.ordinal()) {
            System.out.println(level + ": " + str);
        }
    }
}

package io.bootique.log4j2.appender;

import org.apache.logging.log4j.core.appender.ConsoleAppender;

public enum ConsoleTarget {

    stdout(ConsoleAppender.Target.SYSTEM_OUT.name()),
    stderr(ConsoleAppender.Target.SYSTEM_ERR.name());

    private String log4jTarget;

    ConsoleTarget(String log4jTarget) {
        this.log4jTarget = log4jTarget;
    }

    public String getLog4jkTarget() {
        return log4jTarget;
    }
}

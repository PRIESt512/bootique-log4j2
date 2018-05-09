package io.bootique.log4j2;

import io.bootique.annotation.BQConfig;
import io.bootique.log4j2.appender.AppenderFactory;
import io.bootique.shutdown.ShutdownManager;
import org.apache.logging.log4j.core.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;

@BQConfig
public class Log4jContextFactory {

    private Log4jLevel level;
    private String logFormat;
    private Collection<AppenderFactory> appenders;
    private boolean useLog4jConfig;
    private boolean debugLog4j;

    public Log4jContextFactory() {
        this.level = Log4jLevel.info;
        //this.loggers = Collections.emptyMap();
        this.appenders = Collections.emptyList();

        this.useLog4jConfig = false;
    }

    private String getLogFormat() {
        return logFormat != null ? logFormat : "%-5p [%d{ISO8601,UTC}] %thread %c{20}: %m%n%rEx";
    }

    public Logger createRootLogger(ShutdownManager shutdownManager, Map<String, Level> defaultLevels) {
        return null;
    }
}

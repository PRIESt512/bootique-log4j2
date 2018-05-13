package io.bootique.log4j;

import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

@BQConfig
public class LoggerFactory {

    private Log4jLevel level;

    private String appenderRef;

    public LoggerFactory() {
        this.level = Log4jLevel.info;
        this.appenderRef = "Stdout";
    }

    @BQConfigProperty("Logging level of a given logger and its children.")
    public void setLevel(Log4jLevel level) {
        this.level = level;
    }

    @BQConfigProperty("Appender for logger")
    public void setAppender(String appenderRef) {
        this.appenderRef = appenderRef;
    }

    public LoggerComponentBuilder createLogger(String loggerName, ConfigurationBuilder<BuiltConfiguration> builder) {
        return builder
                .newLogger(loggerName, Level.toLevel(this.level.name(), Level.INFO))
                .add(builder.newAppenderRef(this.appenderRef));
    }
}

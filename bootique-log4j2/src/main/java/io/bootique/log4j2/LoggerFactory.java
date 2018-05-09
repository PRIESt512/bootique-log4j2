package io.bootique.log4j2;

import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

@BQConfig
public class LoggerFactory {

    private Log4jLevel level;

    public LoggerFactory() {
        this.level = Log4jLevel.info;
    }

    @BQConfigProperty("Logging level of a given logger and its children.")
    public void setLevel(Log4jLevel level) {
        this.level = level;
    }

    public void configLogger(String loggerName, ConfigurationBuilder<BuiltConfiguration> builder) {
        //Logger logger = context.getLogger(loggerName);
        //logger.setLevel(Level.toLevel(level.name(), Level.INFO));
    }
}

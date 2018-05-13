package io.bootique.log4j.appender;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import io.bootique.config.PolymorphicConfiguration;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

@BQConfig("Appender of a given type.")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = ConsoleAppenderFactory.class)
public abstract class AppenderFactory implements PolymorphicConfiguration {

    private String logFormat;

    /**
     * @return configured log format
     */
    public String getLogFormat() {
        return logFormat;
    }

    @BQConfigProperty("Log format specification compatible with Log4j2 framework. If not set, the value is propagated " +
            "from the parent configuration.")
    public void setLogFormat(String logFormat) {
        this.logFormat = logFormat;
    }

    protected LayoutComponentBuilder createLayout(ConfigurationBuilder<BuiltConfiguration> builder, String defaultLogFormat) {
        String logFormat = this.logFormat != null ? this.logFormat : defaultLogFormat;

        return builder
                .newLayout("PatternLayout")
                .addAttribute("pattern", logFormat);
    }

    public abstract AppenderComponentBuilder createAppender(ConfigurationBuilder<BuiltConfiguration> builder, String defaultLogFormat);
}

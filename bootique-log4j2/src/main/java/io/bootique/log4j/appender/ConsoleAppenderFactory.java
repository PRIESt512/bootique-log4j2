package io.bootique.log4j.appender;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;

import java.util.Objects;

@JsonTypeName("console")
@BQConfig("Appender that prints its output to stdout or stderr.")
public class ConsoleAppenderFactory extends AppenderFactory {

    private ConsoleTarget target;

    public ConsoleAppenderFactory() {
        this.target = ConsoleTarget.stdout;
    }

    /**
     * @return configured target (stdout or stderr).
     */
    public ConsoleTarget getTarget() {
        return target;
    }

    /**
     * Sets whether the appender should log to stderr or stdout. "stdout" is the default.
     *
     * @param target either "stdout" or "stderr".
     */
    @BQConfigProperty("Whether the log should be sent to stdout or stderr. The default is 'stdout'")
    public void setTarget(ConsoleTarget target) {
        this.target = Objects.requireNonNull(target);
    }

    @Override
    public AppenderComponentBuilder createAppender(ConfigurationBuilder<BuiltConfiguration> builder, String defaultLogFormat) {
        AppenderComponentBuilder appenderBuilder = builder
                .newAppender("Stdout", "CONSOLE")
                .addAttribute("target", target.getLog4jkTarget());

        return appenderBuilder.add(createLayout(builder, defaultLogFormat));
    }
}

package io.bootique.log4j;

import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import io.bootique.log4j.appender.AppenderFactory;
import io.bootique.log4j.appender.ConsoleAppenderFactory;
import io.bootique.shutdown.ShutdownManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.slf4j.ILoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory.newConfigurationBuilder;

@BQConfig
public class Log4jContextFactory {

    private Log4jLevel level;
    private String logFormat;
    private Collection<AppenderFactory> appenders;
    private Map<String, LoggerFactory> loggers;
    private boolean useLog4jConfig;
    private boolean debugLog4j;

    public Log4jContextFactory() {
        this.level = Log4jLevel.info;
        this.loggers = Collections.emptyMap();
        this.appenders = Collections.emptyList();

        this.useLog4jConfig = false;
    }

    private String getLogFormat() {
        return logFormat != null ? logFormat : "%-5p [%d{ISO8601,UTC}] %thread %c{20}: %m%n%rEx";
    }

    public Logger createRootLogger(ShutdownManager shutdownManager, Map<String, java.util.logging.Level> defaultLevels) {

        LoggerContext ctx;
        if (!useLog4jConfig) {
            ConfigurationBuilder<BuiltConfiguration> builder = createLog4jBuilder();
            Map<String, LoggerFactory> loggers = mergeLevels(defaultLevels);

            configLog4jContext(builder, loggers);
            ctx = Configurator.initialize(builder.build());

        } else {
            ctx = (LoggerContext) LogManager.getContext(false);
        }

        shutdownManager.addShutdownHook(ctx::stop);
        //rerouteJUL();
        return ctx.getRootLogger();
    }

    protected ConfigurationBuilder<BuiltConfiguration> createLog4jBuilder() {
        return newConfigurationBuilder();
    }

    protected void configLog4jContext(ConfigurationBuilder<BuiltConfiguration> builder, Map<String, LoggerFactory> loggers) {
        if (appenders.isEmpty()) {
            setAppenders(Collections.singletonList(new ConsoleAppenderFactory()));
        }

        loggers.forEach((name, lf) -> builder.add(lf.createLogger(name, builder)));

        appenders.forEach(item -> builder.add(item.createAppender(builder, getLogFormat())));

        builder.add(builder.newRootLogger(Level.toLevel(level.name(), Level.INFO)).add(builder.newAppenderRef("Stdout")));
    }

    void rerouteJUL() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    /**
     * Merges a map of logging levels with this factory loggers configuration, returning a new map with combined
     * configuration. Factory logger levels take precedence over the provided levels argument (i.e. configuration
     * overrides code settings).
     *
     * @param levels a map of levels keyed by logger name.
     * @return a new map that is combination of factory loggers config and provided set of levels.
     */
    protected Map<String, LoggerFactory> mergeLevels(Map<String, java.util.logging.Level> levels) {
        if (levels.isEmpty()) {
            return this.loggers;
        }

        Map<String, LoggerFactory> merged = new HashMap<>(loggers);

        levels.forEach((name, level) -> {

            LoggerFactory factory = loggers.get(name);
            if (factory == null) {
                factory = new LoggerFactory();
                factory.setLevel(mapJULLevel(level));

                merged.put(name, factory);
            }
        });

        return merged;
    }

    protected Log4jLevel mapJULLevel(java.util.logging.Level level) {
        return JulLevel.valueOf(level.getName()).getLevel();
    }

    /**
     * @return default log level.
     */
    public Log4jLevel getLevel() {
        return level;
    }

    @BQConfigProperty("Root log level. Can be overridden by individual loggers. The default is 'info'.")
    public void setLevel(Log4jLevel level) {
        this.level = level;
    }

    /**
     * @return collection of log level configurations.
     */
    public Map<String, LoggerFactory> getLoggers() {
        return loggers;
    }

    /**
     * @return collection of appender configurations.
     */
    public Collection<AppenderFactory> getAppenders() {
        return appenders;
    }

    @BQConfigProperty("One or more appenders that will render the logs to console, file, etc. If the list is empty, " +
            "console appender is used with default settings.")
    public void setAppenders(Collection<AppenderFactory> appenders) {
        this.appenders = appenders;
    }

    /**
     * If true, all other log4j configuration present in YAML is ignored and
     * the user is expected to provide its own config file per
     * <a href="???">Log4j2
     * documentation</a>.
     *
     * @param useLog4jConfig if true, all other log4j configuration present in YAML is
     *                       ignored.
     */
    @BQConfigProperty("If true, all Bootique log4j settings are ignored and the user is expected to provide its own " +
            "config file per Log4j2 documentation. This is only needed for a few advanced options not directly " +
            "available via Bootique config. So the value should stay false (which is the default).")
    public void setUseLog4jConfig(boolean useLog4jConfig) {
        this.useLog4jConfig = useLog4jConfig;
    }

    /**
     * Sets whether to debug Log4j2 startup and configuration loading.
     *
     * @param debugLog4j if true, turns on tracing of Logback startup.
     * @since 0.13
     */
    @BQConfigProperty("If true, Log4j configuration debugging information will be printed to console. Helps to deal" +
            " with Log4j configuration issues.")
    public void setDebugLog4j(boolean debugLog4j) {
        this.debugLog4j = debugLog4j;
    }

    /**
     * @param logFormat Log format specification used by all appenders unless redefined for a given appender.
     */
    @BQConfigProperty("Log format specification used by child appenders unless redefined at the appender level, or not " +
            "relevant for a given type of appender. The spec is " +
            "compatible with Log4j framework. The default is '%-5p [%d{ISO8601,UTC}] %thread %c{20}: %m%n%rEx'")
    public void setLogFormat(String logFormat) {
        this.logFormat = logFormat;
    }

    private enum JulLevel {

        ALL(Log4jLevel.all),
        CONFIG(Log4jLevel.debug),
        FINE(Log4jLevel.debug),
        FINER(Log4jLevel.debug),
        FINEST(Log4jLevel.trace),
        INFO(Log4jLevel.info),
        OFF(Log4jLevel.off),
        SEVERE(Log4jLevel.error),
        WARNING(Log4jLevel.warn);

        private Log4jLevel level;

        JulLevel(Log4jLevel level) {
            this.level = level;
        }

        public Log4jLevel getLevel() {
            return level;
        }
    }
}

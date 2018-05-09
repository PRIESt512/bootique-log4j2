package io.bootique.log4j2;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.bootique.ConfigModule;
import io.bootique.annotation.LogLevels;
import io.bootique.config.ConfigurationFactory;
import io.bootique.shutdown.ShutdownManager;
import org.apache.logging.log4j.core.Logger;

import java.util.Map;
import java.util.logging.Level;


public class Log4jModule extends ConfigModule {

    public Log4jModule(String prefix) {
        super(prefix);
    }

    public Log4jModule() {
    }

    @Override
    protected String defaultConfigPrefix() {
        return "log";
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(LogInitTrigger.class).asEagerSingleton();
    }

    @Singleton
    @Provides
    Logger provideRootLogger(ConfigurationFactory configFactory,
                             ShutdownManager shutdownManager,
                             @LogLevels Map<String, Level> defaultLevels) {

        return configFactory
                .config(Log4jContextFactory.class, configPrefix)
                .createRootLogger(shutdownManager, defaultLevels);
    }

    static class LogInitTrigger {

        @Inject
        public LogInitTrigger(Logger rootLogger) {
            rootLogger.debug("Log4j2 started");
        }
    }
}

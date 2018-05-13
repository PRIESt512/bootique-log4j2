package io.bootique.log4j;

import com.google.inject.Module;
import io.bootique.BQModule;
import io.bootique.BQModuleProvider;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public class Log4jModuleProvider implements BQModuleProvider {

    @Override
    public Module module() {
        return new Log4jModule();
    }

    @Override
    public Map<String, Type> configs() {
        // TODO: config prefix is hardcoded. Refactor away from ConfigModule, and make provider
        // generate config prefix, reusing it in metadata...
        return Collections.singletonMap("log", Log4jContextFactory.class);
    }

    @Override
    public BQModule.Builder moduleBuilder() {
        return BQModuleProvider.super
                .moduleBuilder()
                .description("Provides logging based on Log4j2 library.");
    }
}

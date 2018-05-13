package io.bootique.log4j.appender;

import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.junit.Before;
import org.junit.Test;

import static org.apache.logging.log4j.core.appender.ConsoleAppender.Target.SYSTEM_ERR;
import static org.apache.logging.log4j.core.appender.ConsoleAppender.Target.SYSTEM_OUT;
import static org.junit.Assert.assertEquals;

public class ConsoleAppenderFactoryTest {

    private ConfigurationBuilder<BuiltConfiguration> builder;

    @Before
    public void before() {
        builder = ConfigurationBuilderFactory.newConfigurationBuilder();
    }

    @Test
    public void testCreateConsoleAppenderTarget_Default() {
        ConsoleAppenderFactory factory = new ConsoleAppenderFactory();
        AppenderComponentBuilder appenderComponentBuilder = factory.createAppender(builder, "");

        assertEquals(SYSTEM_OUT.name(), appenderComponentBuilder.build().getAttributes().get("target"));
    }

    @Test
    public void testCreateConsoleAppenderTarget_Stderr() {
        ConsoleAppenderFactory factory = new ConsoleAppenderFactory();
        factory.setTarget(ConsoleTarget.stderr);
        AppenderComponentBuilder appenderComponentBuilder = factory.createAppender(builder, "");

        assertEquals(SYSTEM_ERR.name(), appenderComponentBuilder.build().getAttributes().get("target"));
    }

    @Test
    public void testCreateConsoleAppenderTarget_Stdout() {
        ConsoleAppenderFactory factory = new ConsoleAppenderFactory();
        factory.setTarget(ConsoleTarget.stdout);
        AppenderComponentBuilder appenderComponentBuilder = factory.createAppender(builder, "");

        assertEquals(SYSTEM_OUT.name(), appenderComponentBuilder.build().getAttributes().get("target"));
    }
}

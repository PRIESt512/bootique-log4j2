package io.bootique.log4j;

import io.bootique.config.ConfigurationFactory;
import io.bootique.log4j.appender.AppenderFactory;
import io.bootique.log4j.appender.ConsoleAppenderFactory;
import io.bootique.log4j.appender.ConsoleTarget;
import io.bootique.log4j.unit.Log4jTestFactory;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class Log4jContextFactoryIT {

    @Rule
    public Log4jTestFactory LOGGER_STACK = new Log4jTestFactory();

    @Test
    public void testInitFromConfig() {
        ConfigurationFactory configFactory = LOGGER_STACK
                .newBQRuntime("classpath:io/bootique/log4j/test.yml")
                .getInstance(ConfigurationFactory.class);

        Log4jContextFactory rootFactory = configFactory.config(Log4jContextFactory.class, "log");

        assertEquals(Log4jLevel.debug, rootFactory.getLevel());

        assertNotNull(rootFactory.getAppenders());

        assertEquals(1, rootFactory.getAppenders().size());

        AppenderFactory[] appenders = rootFactory.getAppenders().toArray(new AppenderFactory[1]);

        assertTrue(appenders[0] instanceof ConsoleAppenderFactory);
        ConsoleAppenderFactory a1 = (ConsoleAppenderFactory) appenders[0];
        assertEquals(ConsoleTarget.stderr, a1.getTarget());
        assertEquals("%c{20}: %m%n", a1.getLogFormat());
    }
}

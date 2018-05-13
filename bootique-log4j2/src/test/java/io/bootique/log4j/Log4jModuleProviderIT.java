package io.bootique.log4j;

import io.bootique.test.junit.BQModuleProviderChecker;
import org.junit.Test;

public class Log4jModuleProviderIT {

    @Test
    public void testAutoLoadable() {
        BQModuleProviderChecker.testAutoLoadable(Log4jModuleProvider.class);
    }

    @Test
    public void testMetadata() {
        BQModuleProviderChecker.testMetadata(Log4jModuleProvider.class);
    }
}

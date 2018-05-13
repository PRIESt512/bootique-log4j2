package io.bootique.log4j.unit;

import io.bootique.BQRuntime;
import io.bootique.log4j.Log4jModule;
import io.bootique.test.junit.BQTestFactory;
import org.apache.logging.log4j.core.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Log4jTestFactory extends BQTestFactory {

    public Logger newRootLogger(String config) {
        return newBQRuntime(config).getInstance(Logger.class);
    }

    public BQRuntime newBQRuntime(String config) {
        String arg0 = "--config=" + Objects.requireNonNull(config);
        return app(arg0).module(Log4jModule.class).createRuntime();
    }

    public void stop() {
        after();
    }

    public void prepareLogDir(String dir) {
        File parent = new File(dir);
        if (parent.exists()) {
            assertTrue(parent.isDirectory());
            asList(parent.list()).stream().forEach(name -> {
                File file = new File(parent, name);
                file.delete();
                assertFalse(file.exists());
            });
        } else {
            parent.mkdirs();
            assertTrue(parent.isDirectory());
        }
    }

    public Map<String, String[]> loglines(String dir, String expectedLogFilePrefix) {

        File parent = new File(dir);
        assertTrue(parent.isDirectory());

        Map<String, String[]> linesByFile = new HashMap<>();

        asList(parent.list()).stream().filter(name -> name.startsWith(expectedLogFilePrefix)).forEach(name -> {

            Path p = Paths.get(parent.getAbsolutePath(), name);
            try {
                linesByFile.put(name, Files.lines(p).toArray(String[]::new));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return linesByFile;
    }
}

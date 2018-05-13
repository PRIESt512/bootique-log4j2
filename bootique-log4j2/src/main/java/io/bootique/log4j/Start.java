package io.bootique.log4j;

import io.bootique.Bootique;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Start {

    //private static final Logger LOG = LoggerFactory.getLogger("io.bootique.log4j.Start");

    public static void main(String[] args) {
        Bootique
                .app("--config=test.yml")
                .autoLoadModules()
                .exec();

        final Logger LOG = LoggerFactory.getLogger("io.bootique.log4j.Start");
        LOG.info("13");
    }
}

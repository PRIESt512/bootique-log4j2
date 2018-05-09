package io.bootique.log4j2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Start {

    private static final Logger LOG = LoggerFactory.getLogger("io.bootique.log4j2.Start");

    public static void main(String[] args) {
        LOG.info("123");
    }
}

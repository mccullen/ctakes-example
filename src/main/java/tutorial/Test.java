package tutorial;

import org.apache.log4j.Logger;

public class Test {
    public static final Logger LOGGER = Logger.getLogger(Test.class.getName());

    public static void main(String[] args) {
        LOGGER.info("info");
        LOGGER.error("error");
        LOGGER.warn("warn");
        LOGGER.debug("debug");
    }
}

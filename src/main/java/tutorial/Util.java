package tutorial;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Util {
    public static final Logger LOGGER = Logger.getLogger(Util.class.getName());

    public static String getProperty(String key) {
        String property = "";
        try {
            InputStream input = new FileInputStream("config.properties");
            Properties props = new Properties();
            props.load(input);
            property = props.getProperty(key);
        } catch (IOException e) {
            LOGGER.error("Error loading file");
        }
        return property;
    }
}

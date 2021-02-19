package tutorial;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Util {
    public static final Logger LOGGER = Logger.getLogger(Util.class.getName());

    public static String getProperty(String key) {
        String property = "";
        try (InputStream input = Util.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties props = new Properties();
            props.load(input);
            property = props.getProperty(key);
        } catch (IOException ex) {
            LOGGER.error("Error loading file");
        }
        String p2 = "";
        try {
            File f = new File("src/main/resources/config.properties");
            String path = f.toString();
            Properties props = new Properties();
            props.load(new FileInputStream(f));
            property = props.getProperty(key);
            System.out.println(property);
        } catch (Exception e) {}
        return property;
    }
}

package tutorial;

import java.io.File;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        //InputStream input = Util.class.getClassLoader().getResourceAsStream("config.properties");
        String filePath = Util.class.getClassLoader().getResource("config.properties").getFile();
        System.out.println("File path: " + filePath);
        File file = new File(filePath);
        if (file != null) {
            System.out.println("Found input from class loader");
        }
        File f = new File("src/main/resources/config.properties");
        if (f != null) {
            System.out.println("Found f from File");
            System.out.println(f.toString());
        }
    }
}

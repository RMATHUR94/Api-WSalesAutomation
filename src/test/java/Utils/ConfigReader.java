package Utils;

// This class reads configuration properties from a file and provides methods to access them.
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties prop;

    static {
        try {
            prop = new Properties();
            FileInputStream fis = new FileInputStream("C:\\Users\\CODECLOUDS-RAHUL\\IdeaProjects\\EBSales\\src\\test\\java\\resources\\config.properties");
            prop.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to load global.properties file", e);
        }
    }

    public static String getGlobalValue(String key) {
        String value = prop.getProperty(key);
        if (value == null) {
            throw new RuntimeException("❌ Property [" + key + "] not found in global.properties");
        }
        return value;
    }
}
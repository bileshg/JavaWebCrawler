package database;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by bilesh on 24/7/16.
 */
public class DatabaseProperties {

    private static String DRIVER;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    static {
        Properties properties = new Properties();
        InputStream inputStream = null;

        try {
            inputStream = DatabaseProperties.class.getResourceAsStream("/config.properties");
            properties.load(inputStream);

            DRIVER = properties.getProperty("database.driver");
            URL = properties.getProperty("database.url");
            USERNAME = properties.getProperty("database.username");
            PASSWORD = properties.getProperty("database.password");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static String getDriver() {
        return DRIVER;
    }

    public static String getURL() {
        return URL;
    }

    public static String getUserName() {
        return USERNAME;
    }

    public static String getPassword() {
        return PASSWORD;
    }
}
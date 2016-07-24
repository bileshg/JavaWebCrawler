package crawler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Created by bilesh on 24/7/16.
 */
public class ConfigProperties {

    private static String URL;
    private static List<String> KEYWORDS;

    static {
        Properties properties = new Properties();
        InputStream inputStream = null;

        try {
            inputStream = ConfigProperties.class.getResourceAsStream("/config.properties");
            properties.load(inputStream);

            URL = properties.getProperty("url");

            String rawKeywords = properties.getProperty("keywords");

            if(rawKeywords != null && !rawKeywords.isEmpty()) {
                KEYWORDS = Arrays.asList(rawKeywords.split(";"));
            } else {
                KEYWORDS = Collections.emptyList();
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static String getURL() {
        return URL;
    }

    public static List<String> getKeywords() {
        return KEYWORDS;
    }
}

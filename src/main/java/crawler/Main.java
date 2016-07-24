package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import database.DatabaseConnection;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final DatabaseConnection databaseConnection = new DatabaseConnection();

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        executorService.execute(() -> {
            try {
                databaseConnection.execute("TRUNCATE Record;");
                processPage(ConfigProperties.getURL());
                databaseConnection.disconnect();
            } catch (SQLException | IOException e) {
                System.out.println(e);
            }
        });
        executorService.shutdown();
    }

    public static void processPage(String url) throws SQLException, IOException {
        // Check if the given URL is already in database
        String sql = "SELECT * FROM Record WHERE URL = '" + url + "'";
        ResultSet rs = databaseConnection.getRecords(sql);
        if (rs.next()) {
            // Do Nothing
        } else {
            // Encode URL
            URLEncoder.encode(url, "UTF-8");

            // Store the URL to database to avoid parsing again
            sql = "INSERT INTO  `Crawler`.`Record` (`URL`) VALUES (?);";
            PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, url);
            stmt.execute();

            Document doc = Jsoup.connect(url).get();

            // Move ahead only if we receive something
            if(doc != null) {
                boolean flag = true;
                for (String keyword : ConfigProperties.getKeywords()) {
                    if (!doc.text().toLowerCase().contains(keyword.toLowerCase())) {
                        flag = false;
                        break;
                    }
                }

                if (flag) {
                    System.out.println(url);
                }

                // Get all links and recursively call the processPage method
                Elements questions = doc.select("a[href]");
                for (Element link : questions) {
                    processPage(link.attr("abs:href"));
                }
            }
        }
    }
}

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

    private static final DatabaseConnection databaseConnection = new DatabaseConnection();

    public static void main(String[] args) {
        try {
            databaseConnection.execute("TRUNCATE Record;");
            processPage("http://www.mit.edu");
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void processPage(String url) throws SQLException, IOException {
        // Check if the given URL is already in database
        String sql = "select * from Record where URL = '" + url + "'";
        ResultSet rs = databaseConnection.getRecords(sql);
        if (rs.next()) {
            // Do Nothing
        } else {
            // Store the URL to database to avoid parsing again
            sql = "INSERT INTO  `Crawler`.`Record` " + "(`URL`) VALUES " + "(?);";
            PreparedStatement stmt = databaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, url);
            stmt.execute();

            // Get useful information
            Document doc = Jsoup.connect("http://www.mit.edu/").get();

            if (doc.text().contains("research")) {
                System.out.println(url);
            }

            // Get all links and recursively call the processPage method
            Elements questions = doc.select("a[href]");
            for (Element link : questions) {
                if (link.attr("href").contains("mit.edu")) {
                    processPage(link.attr("abs:href"));
                }
            }
        }
    }
}

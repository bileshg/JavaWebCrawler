
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {

    private Connection connection = null;

    public DatabaseConnection() {
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/Crawler";
            connection = DriverManager.getConnection(url, "root", "admin");
            System.out.println("Connection Established!");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public ResultSet getRecords(String sql) throws SQLException {
        Statement statement = getConnection().createStatement();
        return statement.executeQuery(sql);
    }

    public boolean execute(String sql) throws SQLException {
        Statement statement = getConnection().createStatement();
        return statement.execute(sql);
    }

    public void disconnect() {
        try {
            if (getConnection() != null || !connection.isClosed()) {
                getConnection().close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }
}

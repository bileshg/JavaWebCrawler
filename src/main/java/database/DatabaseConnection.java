package database;

import java.sql.*;

public class DatabaseConnection {

    private Connection connection = null;

    public DatabaseConnection() {

        try {
            Class.forName(DatabaseProperties.getDriver());

            connection = DriverManager.getConnection(
                    DatabaseProperties.getURL(),
                    DatabaseProperties.getUserName(),
                    DatabaseProperties.getPassword()
            );
            System.out.println("Connection Established!");
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex);
        }

    }

    public ResultSet getRecords(String sql) throws SQLException {
        Statement statement = getConnection().createStatement();
        return statement.executeQuery(sql);
    }

    public int getCount() throws SQLException {
        Statement statement = getConnection().createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM Record");
        rs.next();
        return rs.getInt(1);
    }

    public boolean execute(String sql) throws SQLException {
        Statement statement = getConnection().createStatement();
        return statement.execute(sql);
    }

    public void disconnect() throws SQLException {
        if (getConnection() != null || !connection.isClosed()) {
            getConnection().close();
        }
    }

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }
}

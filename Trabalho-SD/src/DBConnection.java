import java.sql.*;

public class DBConnection {

    // Replace <UTORid> and <DB_PASSWORD> with your login credentials.
    public static final String DRIVER   = "com.amazon.redshift.jdbc4.Driver";
    public static final String URL      = "jdbc:redshift://sd-cache.cocucpsul5bc.us-west-2.rds.amazonaws.com:5432/sd_cache";
    public static final String UID      = "mrmello";
    public static final String PASSWORD = "mrm241192";

    private Connection con = null;

    public DBConnection() {
    }

    public void connect() {
        try {
            Class.forName(DRIVER);
            this.con = DriverManager.getConnection(URL, UID, PASSWORD);
        } catch(ClassNotFoundException e) {
            System.err.println("ClassNotFoundException: " + e.getMessage());
            this.con = null;
        } catch(SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            this.con = null;
        }
    }

    public void disconnect() {
        try {
            this.con.close();
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return this.con;
    }
    
}

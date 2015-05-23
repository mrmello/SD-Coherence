import java.sql.*;

public class DBConnection {

    // Replace <UTORid> and <DB_PASSWORD> with your login credentials.
    public static final String DRIVER   = "org.postgresql.Driver";
    public static final String URL      = "jdbc:postgresql://localhost/demello7";
    public static final String UID      = "demello7";
    public static final String PASSWORD = "14178";

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

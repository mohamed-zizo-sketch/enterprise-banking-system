package pkgfinal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages database connectivity with graceful fallback.
 * Attempts Oracle Database XE connectivity first; if unavailable,
 * flags the system to run in Standalone Resilient Mode.
 */
public class dbConn {

    private static final String ORACLE_URL = System.getProperty("db.url", "jdbc:oracle:thin:ADVANCED/123@localhost:1521/XE");
    private static Boolean oracleAvailable = null;

    /**
     * Attempts to acquire an Oracle database connection.
     * Returns null if Oracle is unavailable, enabling standalone fallback.
     */
    public static Connection DBConnection() {
        Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(ORACLE_URL);
            oracleAvailable = true;
        } catch (ClassNotFoundException | SQLException ex) {
            oracleAvailable = false;
        }
        return conn;
    }

    /**
     * Checks if the Oracle database server is currently reachable.
     */
    public static boolean isOracleAvailable() {
        if (oracleAvailable == null) {
            try (Connection c = DBConnection()) {
                oracleAvailable = (c != null && !c.isClosed());
            } catch (Exception e) {
                oracleAvailable = false;
            }
        }
        return Boolean.TRUE.equals(oracleAvailable);
    }
}

package fatec.grupodois.endurance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBConexao
{
    private static final String USERNAME = "ADMIN";
    private static final String PASSWORD = "8aYBPHjr7oudm8";
    private static final String DATABASE_URL = "jdbc:oracle:thin:@DB202109231147_high?TNS_ADMIN=./key";

    public static Connection abrirConexao() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn;
        try {
            conn = DriverManager.getConnection(DATABASE_URL,USERNAME,PASSWORD);
        } catch(SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return conn;
    }
}

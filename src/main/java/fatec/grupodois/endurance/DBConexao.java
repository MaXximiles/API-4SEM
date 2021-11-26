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

    public static Connection abrirConexao() throws Exception {
        Class.forName("oracle.jdbc.OracleDriver");
        return DriverManager.getConnection(DATABASE_URL,USERNAME,PASSWORD);
    }

    public static void fecharConexao(Connection conectar) throws SQLException
    {
        conectar.close();
    }
}

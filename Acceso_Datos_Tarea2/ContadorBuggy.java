import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContadorBuggy {
    
    public static void main(String[] args) {
        String url = "jdbc:hsqldb:file:./contadores_atomico;hsqldb.lock_file=false";
        
        try (Connection con = DriverManager.getConnection(url, "SA", "")) {
            con.setAutoCommit(false);
            
            inicializarBD(con);
            
            String sqlActualiza = "update contadores set cuenta = cuenta + 1 where nombre='contador1'";
            
            try (PreparedStatement actualiza = con.prepareStatement(sqlActualiza)) {
                for (int i = 0; i < 1000; i++) {
                    actualiza.executeUpdate();
                }
                con.commit();
                
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
            
            // Verificar resultado
            try (Statement consulta = con.createStatement();
                 ResultSet res = consulta.executeQuery("select cuenta from contadores where nombre='contador1'")) {
                if (res.next()) {
                    System.out.println("HSQLDB - Valor final: " + res.getInt(1));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void inicializarBD(Connection con) throws SQLException {
        try (Statement stmt = con.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS contadores (nombre VARCHAR(50) PRIMARY KEY, cuenta INT)");
            
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM contadores WHERE nombre='contador1'");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO contadores VALUES ('contador1', 0)");
            }
            con.commit();
        }
    }
}
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContadorEnBD {

    public static void main(String[] args) {
        final String sqlConsulta = "SELECT cuenta FROM contadores WHERE nombre=?";
        final String sqlActualización = "UPDATE contadores SET cuenta=? WHERE nombre=?";
        final String claveContador = "contador1";

        try {
            Connection connection = DriverManager.getConnection(
                "jdbc:hsqldb:file:./contadores_atomico;hsqldb.lock_file=false", "SA", "");
            
            // Inicializar la base de datos 
            inicializarBD(connection);
            
            PreparedStatement consulta = connection.prepareStatement(sqlConsulta);
            PreparedStatement actualización = connection.prepareStatement(sqlActualización);
            int cuenta = 0;

            consulta.setString(1, claveContador);
            actualización.setString(2, claveContador);
            
            for (int i = 0; i < 1000; i++) {
                ResultSet res = consulta.executeQuery();
                if (res.next()) {
                    cuenta = res.getInt(1) + 1;
                    actualización.setInt(1, cuenta);
                    actualización.executeUpdate();
                } else {
                    System.out.println("Error: No se encontró el contador");
                    break;
                }
            }
            System.out.println("Valor final: " + cuenta);

        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para inicializar la base de datos
    private static void inicializarBD(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Crear tabla si no existe
            stmt.execute("CREATE TABLE IF NOT EXISTS contadores (nombre VARCHAR(50) PRIMARY KEY, cuenta INT)");
            
            // Verificar si existe el registro, si no existe insertarlo
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM contadores WHERE nombre='contador1'");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO contadores VALUES ('contador1', 0)");
                System.out.println("Contador inicializado a 0");
            }
        }
    }
}
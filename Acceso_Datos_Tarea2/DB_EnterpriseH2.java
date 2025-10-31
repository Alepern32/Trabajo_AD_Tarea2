import java.io.File;
import java.sql.*;
import java.util.Scanner;

public class DB_EnterpriseH2 {
	// Hemos usado el gestor de bases de datos H2
	private static final String URL = "jdbc:h2:file:./db_empresa_h2;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1";
	private static final String USER = "SA";
    private static final String PASSWORD = "";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Class.forName("org.h2.Driver");
            eliminarArchivosBD();
            inicializarBaseDatos();
            menuPrincipal();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para inicializar la base de datos H2
    private static void inicializarBaseDatos() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Verificar si las tablas ya existen
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, "T_EMPLEADOS", null);
            
            if (!tables.next()) {
                // Las tablas no existen, crearlas
            	crearEsquemaCompleto(conn);
                System.out.println("Creando base de datos H2...");
            } else {
                System.out.println("Base de datos H2 ya existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al inicializar BD H2: " + e.getMessage());
        }
    }   
    
    private static void eliminarArchivosBD() {
        try {
            File dbFile1 = new File("db_empresa_h2.mv.db");
            File dbFile2 = new File("db_empresa_h2.trace.db");
            
            if (dbFile1.exists()) {
                if (dbFile1.delete()) {
                    System.out.println("Archivo de BD anterior eliminado: " + dbFile1.getName());
                } else {
                    System.out.println("No se pudo eliminar el archivo: " + dbFile1.getName());
                }
            }
            
            if (dbFile2.exists()) {
                if (dbFile2.delete()) {
                    System.out.println("Archivo de trazas eliminado: " + dbFile2.getName());
                }
            }
        } catch (Exception e) {
            System.out.println("Advertencia: No se pudieron eliminar archivos de BD: " + e.getMessage());
            
        }
    }
    
    private static void crearEsquemaCompleto(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        
        // Crear tablas
        stmt.execute("CREATE TABLE t_centros (" +
                    "codigo_centro INTEGER PRIMARY KEY, " +
                    "nombre_centro VARCHAR(21) NOT NULL, " +
                    "ubicacion VARCHAR(50) NOT NULL)");
        
        stmt.execute("CREATE TABLE t_departamentos (" +
                    "codigo_departamento INTEGER PRIMARY KEY, " +
                    "codigo_centro INTEGER NOT NULL, " +
                    "codigo_director INTEGER NOT NULL, " +
                    "tipo_director CHAR(1) NOT NULL, " +
                    "presupuesto_departamento DECIMAL(10,2) NOT NULL, " +
                    "codigo_departamento_superior INTEGER, " +
                    "nombre_departamento VARCHAR(50) NOT NULL, " +
                    "FOREIGN KEY (codigo_centro) REFERENCES t_centros(codigo_centro))");
        
        stmt.execute("CREATE TABLE t_empleados (" +
                    "codigo_empleado INTEGER PRIMARY KEY, " +
                    "codigo_departamento INTEGER NOT NULL, " +
                    "extension_telefonica_empleado SMALLINT NOT NULL, " +
                    "fecha_nacimiento_empleado DATE NOT NULL, " +
                    "fecha_ingreso_empleado DATE NOT NULL, " +
                    "salario_base_empleado DECIMAL(10,2) NOT NULL, " +
                    "comision_empleado DECIMAL(10,2), " +
                    "numero_hijos_empleado SMALLINT NOT NULL, " +
                    "nombre_empleado VARCHAR(50) NOT NULL, " +
                    "FOREIGN KEY (codigo_departamento) REFERENCES t_departamentos(codigo_departamento))");
        
        // Insertar datos
        stmt.execute("INSERT INTO t_centros VALUES (10, 'SEDE CENTRAL', 'C/ ALCALA, 820, MADRID')");
        stmt.execute("INSERT INTO t_centros VALUES (20, 'RELACION CON CLIENTES', 'C/ ATOCHA, 405, MADRID')");
        
        stmt.execute("INSERT INTO t_departamentos VALUES (100, 10, 260, 'P', 120000, NULL, 'DIRECCION GENERAL')");
        stmt.execute("INSERT INTO t_departamentos VALUES (110, 20, 180, 'P', 15000, 100, 'DIRECCION COMERCIAL')");
        stmt.execute("INSERT INTO t_departamentos VALUES (111, 20, 180, 'F', 11000, 110, 'SECTOR INDUSTRIAL')");
        stmt.execute("INSERT INTO t_departamentos VALUES (112, 20, 270, 'P', 9000, 110, 'SECTOR SERVICIOS')");
        stmt.execute("INSERT INTO t_departamentos VALUES (120, 10, 150, 'F', 3000, 100, 'ORGANIZACION')");
        stmt.execute("INSERT INTO t_departamentos VALUES (121, 10, 150, 'P', 2000, 120, 'PERSONAL')");
        stmt.execute("INSERT INTO t_departamentos VALUES (122, 10, 350, 'P', 6000, 120, 'PROCESO DE DATOS')");
        stmt.execute("INSERT INTO t_departamentos VALUES (130, 10, 310, 'P', 2000, 100, 'FINANZAS')");
        
        stmt.execute("INSERT INTO t_empleados VALUES (110, 121, 350, '1949-10-11', '1970-02-15', 3100, NULL, 3, 'PONS, CESAR')");
        stmt.execute("INSERT INTO t_empleados VALUES (120, 112, 840, '1955-06-09', '1988-10-01', 3500, 1100, 1, 'LASA, MARIO')");
        stmt.execute("INSERT INTO t_empleados VALUES (130, 112, 810, '1965-09-09', '1989-02-01', 2900, 1100, 2, 'TEROL, LUCIANO')");
        stmt.execute("INSERT INTO t_empleados VALUES (150, 121, 340, '1950-08-10', '1968-01-15', 4400, NULL, 0, 'PEREZ, JULIO')");
        stmt.execute("INSERT INTO t_empleados VALUES (160, 111, 740, '1959-07-09', '1988-11-11', 3100, 1100, 2, 'AGUIRRE, AUREO')");
        stmt.execute("INSERT INTO t_empleados VALUES (180, 110, 508, '1954-10-18', '1976-03-18', 4800, 500, 2, 'PEREZ, MARCOS')");
        
        System.out.println("Base de datos H2 en memoria creada exitosamente!");
        stmt.close();
    }


    private static void menuPrincipal() {
        while (true) {
            System.out.println("\n=== GESTIÓN EMPRESA (H2) ===");
            System.out.println("1. Ver empleados");
            System.out.println("2. Agregar empleado");
            System.out.println("3. Buscar empleados");
            System.out.println("4. Ver departamentos");
            System.out.println("5. Eliminar empleado");
            System.out.println("6. Información completa con JOINs");
            System.out.println("7. Salir");
            System.out.print("Elige: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1: verEmpleados(); break;
                case 2: agregarEmpleado(); break;
                case 3: buscarEmpleados(); break;
                case 4: verDepartamentos(); break;
                case 5: eliminarEmpleado(); break;
                case 6: informacionCompletaJoins(); break;
                case 7: 
                    System.out.println("¡Adiós!");
                    return;
                default: 
                    System.out.println("Opción no válida");
            }
        }
    }

    // VER TODOS LOS EMPLEADOS
    private static void verEmpleados() {
        String sql = "SELECT e.*, d.nombre_departamento FROM t_empleados e " +
                    "JOIN t_departamentos d ON e.codigo_departamento = d.codigo_departamento";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- EMPLEADOS ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("codigo_empleado") + 
                                 " | Nombre: " + rs.getString("nombre_empleado") +
                                 " | Depto: " + rs.getString("nombre_departamento") +
                                 " | Salario: " + rs.getDouble("salario_base_empleado") +
                                 " | Ingreso: " + rs.getDate("fecha_ingreso_empleado"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // AGREGAR EMPLEADO
    private static void agregarEmpleado() {
        try {
            System.out.println("\n--- NUEVO EMPLEADO ---");
            System.out.print("Código: ");
            int codigo = scanner.nextInt();
            System.out.print("Departamento: ");
            int depto = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Salario: ");
            double salario = scanner.nextDouble();
            scanner.nextLine(); // Limpiar buffer
            System.out.print("Fecha ingreso (YYYY-MM-DD): ");
            String fecha = scanner.nextLine();

            String sql = "INSERT INTO t_empleados (codigo_empleado, codigo_departamento, " +
                        "extension_telefonica_empleado, fecha_nacimiento_empleado, " +
                        "fecha_ingreso_empleado, salario_base_empleado, comision_empleado, " +
                        "numero_hijos_empleado, nombre_empleado) VALUES (?, ?, 100, '1990-01-01', ?, ?, 0, 0, ?)";

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, codigo);
                pstmt.setInt(2, depto);
                pstmt.setString(3, fecha);
                pstmt.setDouble(4, salario);
                pstmt.setString(5, nombre);

                pstmt.executeUpdate();
                System.out.println("Empleado agregado!");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // BUSCAR EMPLEADOS
    private static void buscarEmpleados() {
        System.out.print("\nBuscar por nombre: ");
        String nombre = scanner.nextLine();

        String sql = "SELECT * FROM t_empleados WHERE nombre_empleado LIKE ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nombre + "%");
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n--- RESULTADOS ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("codigo_empleado") + 
                                 " | Nombre: " + rs.getString("nombre_empleado") +
                                 " | Salario: " + rs.getDouble("salario_base_empleado") +
                                 " | Ingreso: " + rs.getDate("fecha_ingreso_empleado"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // VER DEPARTAMENTOS
    private static void verDepartamentos() {
        String sql = "SELECT * FROM t_departamentos";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- DEPARTAMENTOS ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("codigo_departamento") + 
                                 " | Nombre: " + rs.getString("nombre_departamento") +
                                 " | Presupuesto: " + rs.getDouble("presupuesto_departamento"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void informacionCompletaJoins() {
        String sql = "SELECT " +
                    "e.codigo_empleado, " +
                    "e.nombre_empleado, " +
                    "e.salario_base_empleado, " +
                    "e.fecha_ingreso_empleado, " +
                    "d.nombre_departamento, " +
                    "d.presupuesto_departamento, " +
                    "c.nombre_centro, " +
                    "c.ubicacion " +
                    "FROM t_empleados e " +
                    "JOIN t_departamentos d ON e.codigo_departamento = d.codigo_departamento " +
                    "JOIN t_centros c ON d.codigo_centro = c.codigo_centro " +
                    "ORDER BY e.codigo_empleado";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- INFORMACIÓN COMPLETA EMPLEADOS (CON JOINS) ---");
            System.out.println("==================================================================================");
            while (rs.next()) {
                System.out.println("ID Empleado: " + rs.getInt("codigo_empleado"));
                System.out.println("Nombre: " + rs.getString("nombre_empleado"));
                System.out.println("Salario: " + rs.getDouble("salario_base_empleado"));
                System.out.println("Fecha Ingreso: " + rs.getDate("fecha_ingreso_empleado"));
                System.out.println("Departamento: " + rs.getString("nombre_departamento"));
                System.out.println("Presupuesto Depto: " + rs.getDouble("presupuesto_departamento"));
                System.out.println("Centro: " + rs.getString("nombre_centro"));
                System.out.println("Ubicación: " + rs.getString("ubicacion"));
                System.out.println("----------------------------------------------------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void eliminarEmpleado() {
        try {
            System.out.println("Ingresa el código del empleado que quieras eliminar: ");
            int codigo = scanner.nextInt();
            
            // Sintaxis H2 para DELETE
            String sql = "DELETE FROM t_empleados WHERE codigo_empleado = ?";
            
            try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql)) {
                
                ps.setInt(1, codigo);
                int filasAfectadas = ps.executeUpdate();
                
                if (filasAfectadas > 0) {
                    System.out.println("Empleado eliminado correctamente");
                } else {
                    System.out.println("No se encontró el empleado con ese código");
                }
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }

}


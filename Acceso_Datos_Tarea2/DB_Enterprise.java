import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB_Enterprise {

    public static void main(String[] args) throws IOException {

        String driverHSQLDB = "";
        String driverH2 = "";
        String nameDB = "";
        String url = "";

        File f = new File("config.ini");
        Properties properties = new Properties();

        /* Si existe nuestro fichero config.ini cargaremos nuestro archivo y lo cargaremos en nuestra
          base de datos */
        if (!f.exists()) {
            /* Si nuestro archivo no existe lo creamos 
            añadiendo dos propeiedades para cada driver que usaremos */

            properties.setProperty("driverH2", "h2");
            properties.setProperty("driverHSQLDB", "hsqldb");

            /* Añadimos otros 2 setProperty para poner 
            en el nombre de la base de datos */
            properties.setProperty("nameDB", "SA");

            properties.store(new FileOutputStream("config.ini"), "Configuracion de la base de datos");
        } else {
             properties.load(new FileInputStream(f));
             driverHSQLDB = properties.getProperty("driverHSQLDB");
             driverH2 = properties.getProperty("driverH2");
             nameDB = properties.getProperty("nameDB");
        }

        if (driverHSQLDB.equals("hsqldb")) {
             url = "jdbc:hsqldb:file:/home/alumno/Escritorio/BBDD/db_empresa;shutdown=true;hsqldb.lock_file=false"; 

        }else if (driverH2.equals("h2")) {
             url = "jdbc:h2:file:./db_empresa_h2;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1"; 
        }
        try{
          Connection conn = DriverManager.getConnection(url, "SA", "");
          System.out.println("La conexion se realizo correctamente " + url);
        }catch(SQLException e){
            e.printStackTrace();
        }
        


    }
    

    
}
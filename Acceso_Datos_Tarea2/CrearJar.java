import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CrearJar {
    public static void main(String[] args) throws IOException {
        System.out.println("=== GENERADOR DE JAR PORTABLE ===");
        
        // Inicilizamos el codigo de la clase DB_EnterpriseHSQLDB.java
        System.out.println("1. Compilando DB_EnterpriseHSQLDB...");
        ProcessBuilder compilar = new ProcessBuilder(
            "javac", "-cp", "hsqldb-2.7.2.jar", "DB_EnterpriseHSQLDB.java"
        );
        Process procesoCompilar = compilar.start();
        try {
            procesoCompilar.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Crear el archivo manifest
        System.out.println("2. Creando MANIFEST.MF...");
        String manifest = 
            "Manifest-Version: 1.0\n" +
            "Main-Class: DB_EnterpriseHSQLDB\n" +
            "Class-Path: hsqldb-2.7.2.jar\n";
        
        Files.write(Paths.get("MANIFEST.MF"), manifest.getBytes());
        
        // Crear el archivo config.ini sino existe 
        System.out.println("3. Creando config.ini...");
        String config = 
            "#Configuración HSQLDB\n" +
            "database.path=./db_empresa\n";
        
        if (!Files.exists(Paths.get("config.ini"))) {
            Files.write(Paths.get("config.ini"), config.getBytes());
        }
        
        // Generar el JAR a apartir del ProcessBuilder
        System.out.println("4. Generando JAR...");
        ProcessBuilder crearJar = new ProcessBuilder(
            "jar", "cfm", "EmpresaHSQLDB.jar", "MANIFEST.MF", 
            "DB_EnterpriseHSQLDB.class", "config.ini"
        );
        Process procesoJar = crearJar.start();
        try {
            procesoJar.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Eliminamos los arhcivos temporales
        System.out.println("5. Limpiando archivos temporales...");
        Files.deleteIfExists(Paths.get("DB_EnterpriseHSQLDB.class"));
        Files.deleteIfExists(Paths.get("MANIFEST.MF"));
        
        System.out.println("\n JAR CREADO EXITOSAMENTE");
        System.out.println("\n ARCHIVOS GENERADOS:");
        System.out.println("   - EmpresaHSQLDB.jar (tu aplicación)");
        System.out.println("   - config.ini (configuración)");
        System.out.println("\n PARA EJECUTAR:");
        System.out.println("   java -jar EmpresaHSQLDB.jar");
        System.out.println("\n PARA PORTAR A OTRA MÁQUINA, COPIA:");
        System.out.println("   1. EmpresaHSQLDB.jar");
        System.out.println("   2. config.ini");
        System.out.println("   3. hsqldb-2.7.2.jar");
    }
}


package Config;

import java.sql.*;

public class Conexion {
    Connection con;
    public Conexion(){
        try {
            Class.forName("org.postgresql.Driver");
            con=DriverManager.getConnection(
                    "jdbc:postgresql://dpg-d27sspu3jp1c73fnei50-a.oregon-postgres.render.com:5432/mi_casita_segura",
                    "admin",
                    "u410rSBv0A4J7cosYMBmybWSN1ouZUv4"
            );
            System.out.println("Conexión a PostgreSQL exitosa");
        } catch (Exception e) {
            System.err.println("Error"+e);
        }
    }
    public Connection getConnection(){
        return con;
    }
}


package Config;

import java.sql.*;

public class Conexion {
    Connection con;
    public Conexion(){
        try {
            String url = System.getenv("DB_URL");
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");
            
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión PostgreSQL exitosa");
        } catch (Exception e) {
            System.err.println("Error en conexión PostgreSQL: " + e.getMessage());
        }
    }
    public Connection getConnection(){
        return con;
    }
}

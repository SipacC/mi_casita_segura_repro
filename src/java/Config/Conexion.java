package Config;

import java.sql.*;

public class Conexion {
    Connection con;
    public Conexion(){
        try {
            Class.forName("org.postgresql.Driver");
            con=DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/mi_casita_segura_local",

                    "jose",
                    "jose"
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

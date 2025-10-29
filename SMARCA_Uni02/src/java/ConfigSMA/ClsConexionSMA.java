package ConfigSMA;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ClsConexionSMA {

    private static Connection con = null;

    
    public ClsConexionSMA() {
        try {
          
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtienda", "root", "");
            System.out.println("Conexión exitosa a la base de datos");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return con;
    }

    public static void cerrarSMA() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
            System.out.println("Conexión cerrada");
        }
    }
}

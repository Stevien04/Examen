package ConfigSMA;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase responsable de gestionar la conexión con la base de datos del sistema
 * de reclamos. Implementa una inicialización segura del driver y expone un
 * método utilitario para obtener nuevas conexiones utilizando las credenciales
 * configuradas para el proyecto.
 */
public class ClsConexionSMA {

   
    private static final String URL = "jdbc:mysql://localhost:3306/db_reclamossma?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    
    static {
        try {
          
            Class.forName("com.mysql.cj.jdbc.Driver");
            
        } catch (ClassNotFoundException ex) {
            throw new ExceptionInInitializerError("No se pudo cargar el driver de MySQL: " + ex.getMessage());
        }
    }

   
    /**
     * Obtiene una nueva conexión activa con la base de datos. El consumidor es
     * responsable de cerrar la conexión mediante try-with-resources.
     *
     * @return conexión válida con la base de datos.
     * @throws SQLException si ocurre un error al establecer la conexión.
     */
    public Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/segurti_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root"; // Senha padrão do MySQL
    private static boolean dbAvailable = true;
    
    public static Connection getConnection() throws SQLException {
        if (!dbAvailable) {
            throw new SQLException("Database não disponível - usando modo offline");
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return conn;
        } catch (ClassNotFoundException e) {
            dbAvailable = false;
            throw new SQLException("Driver MySQL não encontrado", e);
        } catch (SQLException e) {
            dbAvailable = false;
            throw new SQLException("Erro de conexão com MySQL: " + e.getMessage(), e);
        }
    }
    
    public static boolean isDatabaseAvailable() {
        return dbAvailable;
    }
    
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
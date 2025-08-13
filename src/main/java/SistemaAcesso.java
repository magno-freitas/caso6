import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SistemaAcesso {
    private Usuario usuarioLogado;
    
    public boolean autenticar(String email, String senha) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT id, nome, email, funcao, ativo FROM usuarios WHERE email = ? AND senha = SHA2(?, 256) AND ativo = TRUE";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, senha);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                usuarioLogado = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("funcao"),
                    rs.getBoolean("ativo")
                );
                
                registrarLog(email, "login", "sucesso");
                return true;
            } else {
                registrarLog(email, "login", "falha");
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro na autenticação: " + e.getMessage());
            registrarLog(email, "login", "falha");
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }
    
    public boolean autorizar(String acao) {
        if (usuarioLogado == null) {
            return false;
        }
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM permissoes WHERE funcao = ? AND acao = ?";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuarioLogado.getFuncao());
            stmt.setString(2, acao);
            
            ResultSet rs = stmt.executeQuery();
            rs.next();
            
            return rs.getInt(1) > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro na autorização: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }
    
    public void logout() {
        if (usuarioLogado != null) {
            registrarLog(usuarioLogado.getEmail(), "logout", "sucesso");
            usuarioLogado = null;
        }
    }
    
    private void registrarLog(String email, String acao, String status) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO logs_acesso (email, acao, status, ip_address) VALUES (?, ?, ?, ?)";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, acao);
            stmt.setString(3, status);
            stmt.setString(4, "127.0.0.1"); // IP simulado
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erro ao registrar log: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }
    
    public List<String> obterPermissoes() {
        List<String> permissoes = new ArrayList<>();
        
        if (usuarioLogado == null) {
            return permissoes;
        }
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT acao, descricao FROM permissoes WHERE funcao = ?";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuarioLogado.getFuncao());
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                permissoes.add(rs.getString("acao") + " - " + rs.getString("descricao"));
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao obter permissões: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        
        return permissoes;
    }
    
    public List<String> obterLogs() {
        List<String> logs = new ArrayList<>();
        Connection conn = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT email, acao, status, timestamp FROM logs_acesso ORDER BY timestamp DESC LIMIT 20";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String log = String.format("%s | %s | %s | %s",
                    rs.getTimestamp("timestamp"),
                    rs.getString("email"),
                    rs.getString("acao"),
                    rs.getString("status")
                );
                logs.add(log);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao obter logs: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        
        return logs;
    }
    
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
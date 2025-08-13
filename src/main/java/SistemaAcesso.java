import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SistemaAcesso {
    private Usuario usuarioLogado;
    private static boolean offlineMode = false;
    private static boolean dbChecked = false;
    
    // Usuários de teste para modo offline
    private static final Map<String, String[]> USUARIOS_TESTE = new HashMap<>();
    static {
        USUARIOS_TESTE.put("ana.s@segurti.com", new String[]{"admin123", "Ana Souza", "Administrador"});
        USUARIOS_TESTE.put("joao.l@segurti.com", new String[]{"user123", "João Lima", "Usuário Comum"});
        USUARIOS_TESTE.put("maria.c@segurti.com", new String[]{"admin456", "Maria Costa", "Administrador"});
        USUARIOS_TESTE.put("pedro.s@segurti.com", new String[]{"user456", "Pedro Silva", "Usuário Comum"});
    }
    
    public boolean autenticar(String email, String senha) {
        // Verificar disponibilidade do banco apenas uma vez
        if (!dbChecked) {
            checkDatabaseAvailability();
            dbChecked = true;
        }
        
        // Tentar conexão com banco primeiro
        if (!offlineMode) {
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
                if (!offlineMode) {
                    System.out.println("⚠ Banco de dados indisponível. Usando modo offline...");
                    offlineMode = true;
                }
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
        
        // Modo offline - usar usuários de teste
        if (USUARIOS_TESTE.containsKey(email)) {
            String[] userData = USUARIOS_TESTE.get(email);
            if (userData[0].equals(senha)) {
                usuarioLogado = new Usuario(1, userData[1], email, userData[2], true);
                System.out.println("✓ Autenticação offline realizada com sucesso!");
                return true;
            }
        }
        
        return false;
    }
    
    public boolean autorizar(String acao) {
        if (usuarioLogado == null) {
            return false;
        }
        
        if (!offlineMode) {
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
                offlineMode = true;
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
        
        // Modo offline - permissões hardcoded
        String funcao = usuarioLogado.getFuncao();
        if ("Administrador".equals(funcao)) {
            return List.of("criar_usuario", "editar_usuario", "excluir_usuario", 
                          "visualizar_relatorios", "gerenciar_sistema", "backup_dados",
                          "visualizar_perfil", "editar_perfil", "visualizar_dados").contains(acao);
        } else if ("Usuário Comum".equals(funcao)) {
            return List.of("visualizar_perfil", "editar_perfil", "visualizar_dados").contains(acao);
        }
        
        return false;
    }
    
    public void logout() {
        if (usuarioLogado != null) {
            registrarLog(usuarioLogado.getEmail(), "logout", "sucesso");
            usuarioLogado = null;
        }
    }
    
    private void registrarLog(String email, String acao, String status) {
        if (offlineMode) {
            return; // Não registra logs em modo offline
        }
        
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
            // Silencioso em modo offline
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }
    
    public List<String> obterPermissoes() {
        List<String> permissoes = new ArrayList<>();
        
        if (usuarioLogado == null) {
            return permissoes;
        }
        
        if (!offlineMode) {
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
                offlineMode = true;
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
        
        // Modo offline - permissões hardcoded
        if (offlineMode || permissoes.isEmpty()) {
            String funcao = usuarioLogado.getFuncao();
            if ("Administrador".equals(funcao)) {
                permissoes.add("criar_usuario - Criar novos usuários");
                permissoes.add("editar_usuario - Editar dados de usuários");
                permissoes.add("excluir_usuario - Excluir usuários");
                permissoes.add("visualizar_relatorios - Visualizar relatórios");
                permissoes.add("gerenciar_sistema - Gerenciar configurações");
                permissoes.add("backup_dados - Realizar backup");
                permissoes.add("visualizar_perfil - Visualizar próprio perfil");
                permissoes.add("editar_perfil - Editar próprio perfil");
                permissoes.add("visualizar_dados - Visualizar dados gerais");
            } else if ("Usuário Comum".equals(funcao)) {
                permissoes.add("visualizar_perfil - Visualizar próprio perfil");
                permissoes.add("editar_perfil - Editar próprio perfil");
                permissoes.add("visualizar_dados - Visualizar dados permitidos");
            }
        }
        
        return permissoes;
    }
    
    public List<String> obterLogs() {
        List<String> logs = new ArrayList<>();
        
        if (!offlineMode) {
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
                offlineMode = true;
            } finally {
                DatabaseConnection.closeConnection(conn);
            }
        }
        
        if (offlineMode || logs.isEmpty()) {
            logs.add("[MODO OFFLINE] Logs não disponíveis sem conexão com banco de dados");
        }
        
        return logs;
    }
    
    private void checkDatabaseAvailability() {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            System.out.println("⚠ Banco de dados indisponível. Sistema iniciado em modo offline.");
            offlineMode = true;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }
    
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
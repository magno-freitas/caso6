import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SistemaAcessoSimulado {
    private Map<String, Usuario> usuarios;
    private Map<String, List<String>> permissoes;
    private List<String> logs;
    private Usuario usuarioLogado;
    
    public SistemaAcessoSimulado() {
        inicializarDados();
    }
    
    private void inicializarDados() {
        usuarios = new HashMap<>();
        usuarios.put("ana.s@segurti.com", new Usuario(1, "Ana Souza", "ana.s@segurti.com", "admin", true));
        usuarios.put("joao.l@segurti.com", new Usuario(2, "João Lima", "joao.l@segurti.com", "usuario", true));
        usuarios.put("maria.c@segurti.com", new Usuario(3, "Maria Costa", "maria.c@segurti.com", "admin", true));
        usuarios.put("pedro.s@segurti.com", new Usuario(4, "Pedro Silva", "pedro.s@segurti.com", "usuario", true));
        
        permissoes = new HashMap<>();
        permissoes.put("admin", Arrays.asList(
            "criar_usuario - Criar novos usuários no sistema",
            "editar_usuario - Editar dados de usuários existentes", 
            "excluir_usuario - Excluir usuários do sistema",
            "visualizar_relatorios - Visualizar relatórios do sistema",
            "gerenciar_sistema - Gerenciar configurações do sistema",
            "backup_dados - Realizar backup dos dados"
        ));
        
        permissoes.put("usuario", Arrays.asList(
            "visualizar_perfil - Visualizar próprio perfil",
            "editar_perfil - Editar próprio perfil", 
            "visualizar_dados - Visualizar dados básicos do sistema"
        ));
        
        logs = new ArrayList<>();
    }
    
    public boolean autenticar(String email, String senha) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        if (usuarios.containsKey(email)) {
            // Simulação de verificação de senha (senhas fixas para demonstração)
            boolean senhaCorreta = false;
            switch (email) {
                case "ana.s@segurti.com": senhaCorreta = "admin123".equals(senha); break;
                case "joao.l@segurti.com": senhaCorreta = "user123".equals(senha); break;
                case "maria.c@segurti.com": senhaCorreta = "admin456".equals(senha); break;
                case "pedro.s@segurti.com": senhaCorreta = "user456".equals(senha); break;
            }
            
            if (senhaCorreta) {
                usuarioLogado = usuarios.get(email);
                logs.add(timestamp + " | " + email + " | login | sucesso");
                return true;
            }
        }
        
        logs.add(timestamp + " | " + email + " | login | falha");
        return false;
    }
    
    public boolean autorizar(String acao) {
        if (usuarioLogado == null) return false;
        
        List<String> userPermissoes = permissoes.get(usuarioLogado.getFuncao());
        return userPermissoes.stream().anyMatch(p -> p.startsWith(acao));
    }
    
    public void logout() {
        if (usuarioLogado != null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            logs.add(timestamp + " | " + usuarioLogado.getEmail() + " | logout | sucesso");
            usuarioLogado = null;
        }
    }
    
    public List<String> obterPermissoes() {
        if (usuarioLogado == null) return new ArrayList<>();
        return permissoes.get(usuarioLogado.getFuncao());
    }
    
    public List<String> obterLogs() {
        return new ArrayList<>(logs);
    }
    
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
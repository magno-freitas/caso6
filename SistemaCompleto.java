import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Usuario {
    private int id;
    private String nome;
    private String email;
    private String funcao;
    private boolean ativo;
    
    public Usuario(int id, String nome, String email, String funcao, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.funcao = funcao;
        this.ativo = ativo;
    }
    
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getFuncao() { return funcao; }
}

class SistemaAcesso {
    private Map<String, Usuario> usuarios;
    private Map<String, List<String>> permissoes;
    private List<String> logs;
    private Usuario usuarioLogado;
    
    public SistemaAcesso() {
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
        return permissoes.get(usuarioLogado.getFuncao()).stream().anyMatch(p -> p.startsWith(acao));
    }
    
    public void logout() {
        if (usuarioLogado != null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            logs.add(timestamp + " | " + usuarioLogado.getEmail() + " | logout | sucesso");
            usuarioLogado = null;
        }
    }
    
    public List<String> obterPermissoes() {
        return usuarioLogado == null ? new ArrayList<>() : permissoes.get(usuarioLogado.getFuncao());
    }
    
    public List<String> obterLogs() { return new ArrayList<>(logs); }
    public Usuario getUsuarioLogado() { return usuarioLogado; }
}

public class SistemaCompleto {
    public static void main(String[] args) {
        SistemaAcesso sistema = new SistemaAcesso();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== SISTEMA DE CONTROLE DE ACESSO - SEGURTI ===\n");
        
        // Simulação automática
        System.out.println("=== SIMULAÇÃO DE TENTATIVAS DE ACESSO ===\n");
        
        String[][] tentativas = {
            {"ana.s@segurti.com", "admin123"},
            {"joao.l@segurti.com", "user123"},
            {"ana.s@segurti.com", "senha_errada"},
            {"usuario_fake@test.com", "123"},
            {"maria.c@segurti.com", "admin456"}
        };
        
        for (String[] tentativa : tentativas) {
            String email = tentativa[0];
            String senha = tentativa[1];
            
            System.out.println("Tentativa de login: " + email);
            
            if (sistema.autenticar(email, senha)) {
                Usuario usuario = sistema.getUsuarioLogado();
                System.out.println("✓ Login realizado com sucesso!");
                System.out.println("  Usuário: " + usuario.getNome());
                System.out.println("  Função: " + usuario.getFuncao());
                
                System.out.println("  Permissões disponíveis:");
                for (String permissao : sistema.obterPermissoes()) {
                    System.out.println("    ✓ " + permissao);
                }
                
                String[] acoesTeste = {"criar_usuario", "visualizar_perfil", "gerenciar_sistema"};
                System.out.println("  Teste de autorização:");
                for (String acao : acoesTeste) {
                    if (sistema.autorizar(acao)) {
                        System.out.println("    ✓ Autorizado para: " + acao);
                    } else {
                        System.out.println("    ✗ Não autorizado para: " + acao);
                    }
                }
                
                sistema.logout();
            } else {
                System.out.println("✗ Falha no login - Credenciais inválidas");
            }
            
            System.out.println("------------------------------------------------------------");
        }
        
        System.out.println("\n=== LOGS DE ACESSO ===");
        for (String log : sistema.obterLogs()) {
            System.out.println(log);
        }
        
        System.out.println("\n=== MODO INTERATIVO ===");
        System.out.println("Digite 'sair' para encerrar");
        
        while (true) {
            if (sistema.getUsuarioLogado() == null) {
                System.out.print("\nEmail: ");
                String email = scanner.nextLine();
                
                if (email.equalsIgnoreCase("sair")) break;
                
                System.out.print("Senha: ");
                String senha = scanner.nextLine();
                
                if (sistema.autenticar(email, senha)) {
                    Usuario usuario = sistema.getUsuarioLogado();
                    System.out.println("\n✓ Bem-vindo, " + usuario.getNome() + "!");
                    System.out.println("Função: " + usuario.getFuncao());
                } else {
                    System.out.println("✗ Credenciais inválidas!");
                }
            } else {
                Usuario usuario = sistema.getUsuarioLogado();
                System.out.println("\n--- Usuário logado: " + usuario.getNome() + " ---");
                System.out.println("1. Ver permissões");
                System.out.println("2. Testar autorização");
                System.out.println("3. Logout");
                System.out.print("Escolha uma opção: ");
                
                String opcao = scanner.nextLine();
                
                switch (opcao) {
                    case "1":
                        System.out.println("\nSuas permissões:");
                        for (String permissao : sistema.obterPermissoes()) {
                            System.out.println("  ✓ " + permissao);
                        }
                        break;
                        
                    case "2":
                        System.out.print("Digite a ação para testar: ");
                        String acao = scanner.nextLine();
                        if (sistema.autorizar(acao)) {
                            System.out.println("✓ Você tem permissão para: " + acao);
                        } else {
                            System.out.println("✗ Você NÃO tem permissão para: " + acao);
                        }
                        break;
                        
                    case "3":
                        sistema.logout();
                        System.out.println("✓ Logout realizado com sucesso!");
                        break;
                        
                    default:
                        System.out.println("Opção inválida!");
                }
            }
        }
        
        scanner.close();
        System.out.println("\nSistema encerrado.");
    }
}
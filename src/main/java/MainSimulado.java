import java.util.List;
import java.util.Scanner;

public class MainSimulado {
    public static void main(String[] args) {
        SistemaAcessoSimulado sistema = new SistemaAcessoSimulado();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== SISTEMA DE CONTROLE DE ACESSO - SEGURTI (SIMULADO) ===\n");
        
        // Simulação automática de tentativas
        System.out.println("=== SIMULAÇÃO DE TENTATIVAS DE ACESSO ===\n");
        
        String[][] tentativas = {
            {"ana.s@segurti.com", "admin123"},      // Válida
            {"joao.l@segurti.com", "user123"},     // Válida  
            {"ana.s@segurti.com", "senha_errada"}, // Inválida
            {"usuario_fake@test.com", "123"},      // Inválida
            {"maria.c@segurti.com", "admin456"}    // Válida
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
                
                // Teste de permissões
                System.out.println("  Permissões disponíveis:");
                List<String> permissoes = sistema.obterPermissoes();
                for (String permissao : permissoes) {
                    System.out.println("    ✓ " + permissao);
                }
                
                // Teste de autorização para ações específicas
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
            
            System.out.println("-".repeat(60));
        }
        
        // Exibir logs
        System.out.println("\n=== LOGS DE ACESSO ===");
        List<String> logs = sistema.obterLogs();
        for (String log : logs) {
            System.out.println(log);
        }
        
        // Menu interativo
        System.out.println("\n=== MODO INTERATIVO ===");
        System.out.println("Digite 'sair' para encerrar");
        
        while (true) {
            if (sistema.getUsuarioLogado() == null) {
                System.out.print("\nEmail: ");
                String email = scanner.nextLine();
                
                if (email.equalsIgnoreCase("sair")) {
                    break;
                }
                
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
                        List<String> permissoes = sistema.obterPermissoes();
                        for (String permissao : permissoes) {
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
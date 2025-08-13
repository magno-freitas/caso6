# Sistema de Controle de Acesso - SegurTI

## Descrição
Sistema de autenticação e autorização desenvolvido em Java com MySQL para o estudo de caso da empresa fictícia SegurTI.

## Tecnologias Utilizadas
- **Java 11+**
- **MySQL 8.0**
- **Maven** (gerenciamento de dependências)
- **JDBC** (conectividade com banco de dados)

## Estrutura do Projeto
```
caso6/
├── src/main/java/
│   ├── Main.java              # Classe principal com simulações
│   ├── SistemaAcesso.java     # Lógica de autenticação/autorização
│   ├── Usuario.java           # Modelo de dados do usuário
│   └── DatabaseConnection.java # Gerenciamento de conexões
├── database_setup.sql         # Script de criação do banco
├── pom.xml                   # Configuração Maven
└── README.md                 # Este arquivo
```

## Configuração do Ambiente

### 1. Banco de Dados MySQL
```bash
# Instalar MySQL Server
# Executar o script database_setup.sql
mysql -u root -p < database_setup.sql
```

### 2. Configuração Java
```bash
# Compilar o projeto
mvn clean compile

# Executar o sistema
mvn exec:java -Dexec.mainClass="Main"
```

## Funcionalidades Implementadas

### Autenticação
- Verificação de credenciais (email/senha)
- Hash SHA-256 para senhas
- Controle de sessão de usuário

### Autorização
- Dois níveis de acesso: Administrador e Usuário Comum
- Verificação de permissões por função
- Controle granular de ações

### Auditoria
- Log completo de tentativas de acesso
- Registro de timestamp e status
- Rastreamento de ações do usuário

## Usuários de Teste

| Nome | Email | Senha | Função |
|------|-------|-------|--------|
| Ana Souza | ana.s@segurti.com | admin123 | Administrador |
| João Lima | joao.l@segurti.com | user123 | Usuário Comum |
| Maria Costa | maria.c@segurti.com | admin456 | Administrador |
| Pedro Silva | pedro.s@segurti.com | user456 | Usuário Comum |

## Permissões por Função

### Administrador
- criar_usuario
- editar_usuario
- excluir_usuario
- visualizar_relatorios
- gerenciar_sistema
- backup_dados

### Usuário Comum
- visualizar_perfil
- editar_perfil
- visualizar_dados

## Execução
1. Configure o MySQL e execute o script SQL
2. Ajuste as credenciais em `DatabaseConnection.java`
3. Execute `mvn clean compile exec:java`
4. O sistema executará simulações automáticas e oferecerá modo interativo

## Medidas de Segurança
- Senhas armazenadas com hash SHA-256
- Validação de entrada de dados
- Controle de sessão
- Log de auditoria completo
- Princípio do menor privilégio
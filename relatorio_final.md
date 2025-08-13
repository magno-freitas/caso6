# RELATÓRIO FINAL - SISTEMA DE CONTROLE DE ACESSO SEGURTI

## 1. EQUIPE DO PROJETO
- **Empresa**: SegurTI Solutions
- **Projeto**: Sistema de Controle de Acesso
- **Tecnologias**: Java 11, MySQL 8.0, Maven

## 2. DESCRIÇÃO DO SISTEMA

O sistema desenvolvido implementa um controle de acesso robusto com arquitetura em camadas:

### Arquitetura
- **Camada de Apresentação**: Interface console interativa
- **Camada de Negócio**: Lógica de autenticação e autorização
- **Camada de Dados**: Persistência em MySQL com JDBC

### Funcionalidades Principais
- Autenticação segura com hash SHA-256
- Autorização baseada em funções (RBAC)
- Auditoria completa com logs
- Interface interativa para testes

## 3. TABELA DE USUÁRIOS IMPLEMENTADA

| ID | Nome | Email | Função | Status |
|----|------|-------|--------|--------|
| 1 | Ana Souza | ana.s@segurti.com | Administrador | Ativo |
| 2 | João Lima | joao.l@segurti.com | Usuário Comum | Ativo |
| 3 | Maria Costa | maria.c@segurti.com | Administrador | Ativo |
| 4 | Pedro Silva | pedro.s@segurti.com | Usuário Comum | Ativo |

## 4. MATRIZ DE PERMISSÕES

### Administrador (admin)
| Ação | Descrição | Autorizado |
|------|-----------|------------|
| criar_usuario | Criar novos usuários no sistema | ✓ |
| editar_usuario | Editar dados de usuários existentes | ✓ |
| excluir_usuario | Excluir usuários do sistema | ✓ |
| visualizar_relatorios | Visualizar relatórios do sistema | ✓ |
| gerenciar_sistema | Gerenciar configurações do sistema | ✓ |
| backup_dados | Realizar backup dos dados | ✓ |

### Usuário Comum (usuario)
| Ação | Descrição | Autorizado |
|------|-----------|------------|
| visualizar_perfil | Visualizar próprio perfil | ✓ |
| editar_perfil | Editar próprio perfil | ✓ |
| visualizar_dados | Visualizar dados básicos do sistema | ✓ |
| criar_usuario | Criar novos usuários no sistema | ✗ |
| gerenciar_sistema | Gerenciar configurações do sistema | ✗ |

## 5. FLUXO DE AUTENTICAÇÃO E AUTORIZAÇÃO

### Processo de Autenticação
```
1. Usuário fornece email e senha
2. Sistema consulta banco de dados
3. Verifica hash SHA-256 da senha
4. Se válido, cria sessão do usuário
5. Registra tentativa no log de auditoria
```

### Processo de Autorização
```
1. Usuário solicita ação específica
2. Sistema verifica se usuário está autenticado
3. Consulta permissões da função do usuário
4. Autoriza ou nega acesso baseado nas permissões
5. Registra ação no log (se executada)
```

## 6. IMPLEMENTAÇÃO DE SEGURANÇA

### Medidas Implementadas
- **Criptografia**: Senhas armazenadas com hash SHA-256
- **Validação**: Verificação de entrada de dados
- **Auditoria**: Log completo de todas as tentativas
- **Controle de Sessão**: Gerenciamento de usuário logado
- **Princípio do Menor Privilégio**: Usuários têm apenas permissões necessárias

### Estrutura do Banco de Dados
```sql
-- Tabelas principais
usuarios (id, nome, email, senha, funcao, ativo, data_criacao)
permissoes (id, funcao, acao, descricao)
logs_acesso (id, email, acao, status, ip_address, timestamp)
```

## 7. RESULTADOS DA SIMULAÇÃO

### Cenários Testados
1. **Login Válido - Administrador**: ✓ Sucesso
2. **Login Válido - Usuário Comum**: ✓ Sucesso
3. **Login com Senha Incorreta**: ✗ Falha registrada
4. **Login com Usuário Inexistente**: ✗ Falha registrada
5. **Teste de Autorização por Função**: ✓ Funcionando corretamente

### Logs Gerados (Exemplo)
```
2024-01-15 10:30:15 | ana.s@segurti.com | login | sucesso
2024-01-15 10:30:45 | ana.s@segurti.com | logout | sucesso
2024-01-15 10:31:00 | joao.l@segurti.com | login | sucesso
2024-01-15 10:31:30 | usuario_fake@test.com | login | falha
```

## 8. VANTAGENS DA SOLUÇÃO

### Técnicas
- **Escalabilidade**: Arquitetura permite expansão
- **Manutenibilidade**: Código organizado em classes
- **Segurança**: Implementação de boas práticas
- **Auditoria**: Rastreamento completo de ações

### Funcionais
- **Flexibilidade**: Fácil adição de novas permissões
- **Usabilidade**: Interface intuitiva
- **Confiabilidade**: Tratamento de erros
- **Performance**: Consultas otimizadas

## 9. POSSÍVEIS MELHORIAS FUTURAS

1. **Interface Gráfica**: Desenvolvimento de GUI
2. **Criptografia Avançada**: Implementação de bcrypt
3. **Autenticação Multifator**: Adição de 2FA
4. **API REST**: Exposição de serviços web
5. **Relatórios**: Dashboard de auditoria

## 10. CONCLUSÃO

O sistema atende completamente aos requisitos do estudo de caso, implementando:
- ✓ Autenticação segura
- ✓ Autorização baseada em funções
- ✓ Dois níveis de acesso (Admin/Usuário)
- ✓ Registro de logs de auditoria
- ✓ Simulações de acesso válidas e inválidas

A solução demonstra aplicação prática dos conceitos de segurança e controle de acesso, utilizando tecnologias modernas e seguindo boas práticas de desenvolvimento.
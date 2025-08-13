# RELATÓRIO DO SISTEMA DE CONTROLE DE ACESSO - SEGURTI

## 1. DESCRIÇÃO DO SISTEMA

O sistema desenvolvido para a empresa SegurTI implementa um controle de acesso robusto com dois níveis hierárquicos:
- **Administrador**: Acesso completo ao sistema
- **Usuário Comum**: Acesso limitado a funcionalidades básicas

## 2. TABELA DE USUÁRIOS

| Nome | Email | Senha | Função |
|------|-------|-------|--------|
| Ana Souza | ana.s@segurti.com | admin123 | Administrador |
| João Lima | joao.l@segurti.com | user123 | Usuário Comum |
| Maria Costa | maria.c@segurti.com | admin456 | Administrador |
| Pedro Silva | pedro.s@segurti.com | user456 | Usuário Comum |

## 3. PERMISSÕES POR NÍVEL

### Administrador
- ✓ Criar usuário
- ✓ Editar usuário
- ✓ Excluir usuário
- ✓ Visualizar relatórios
- ✓ Gerenciar sistema
- ✓ Backup de dados

### Usuário Comum
- ✓ Visualizar perfil
- ✓ Editar perfil
- ✓ Visualizar dados

## 4. FLUXO DE AUTENTICAÇÃO E AUTORIZAÇÃO

### Autenticação
1. Usuário fornece email e senha
2. Sistema verifica se email existe na base
3. Sistema compara hash da senha fornecida com hash armazenado
4. Se válido, cria sessão do usuário
5. Registra tentativa no log (sucesso/falha)

### Autorização
1. Para cada ação solicitada, sistema verifica se usuário está logado
2. Consulta permissões da função do usuário
3. Autoriza ou nega acesso baseado nas permissões

## 5. MEDIDAS DE SEGURANÇA IMPLEMENTADAS

- **Hash de senhas**: Senhas armazenadas com SHA-256
- **Log de auditoria**: Registro de todas as tentativas de acesso
- **Controle de sessão**: Verificação de usuário logado
- **Princípio do menor privilégio**: Usuários têm apenas permissões necessárias

## 6. RESULTADOS DA SIMULAÇÃO

### Tentativas Válidas
- ✓ ana.s@segurti.com (Administrador) - Login realizado
- ✓ joao.l@segurti.com (Usuário) - Login realizado  
- ✓ maria.c@segurti.com (Administrador) - Login realizado

### Tentativas Inválidas
- ✗ ana.s@segurti.com com senha incorreta - Falha
- ✗ usuario_inexistente@test.com - Falha

### Logs Gerados
Todos os acessos foram registrados com timestamp, email, ação e status para auditoria.

## 7. CONCLUSÃO

O sistema atende aos requisitos de segurança estabelecidos, implementando controles adequados de autenticação e autorização com registro completo de auditoria.
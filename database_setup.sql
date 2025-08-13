-- Criação do banco de dados
CREATE DATABASE segurti_db;
USE segurti_db;

-- Tabela de usuários
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    funcao ENUM('admin', 'usuario') NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de permissões
CREATE TABLE permissoes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    funcao ENUM('admin', 'usuario') NOT NULL,
    acao VARCHAR(50) NOT NULL,
    descricao VARCHAR(200)
);

-- Tabela de logs de acesso
CREATE TABLE logs_acesso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    acao VARCHAR(50) NOT NULL,
    status ENUM('sucesso', 'falha') NOT NULL,
    ip_address VARCHAR(45),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Inserir usuários (senhas em hash SHA-256)
INSERT INTO usuarios (nome, email, senha, funcao) VALUES
('Ana Souza', 'ana.s@segurti.com', SHA2('admin123', 256), 'admin'),
('João Lima', 'joao.l@segurti.com', SHA2('user123', 256), 'usuario'),
('Maria Costa', 'maria.c@segurti.com', SHA2('admin456', 256), 'admin'),
('Pedro Silva', 'pedro.s@segurti.com', SHA2('user456', 256), 'usuario');

-- Inserir permissões para administradores
INSERT INTO permissoes (funcao, acao, descricao) VALUES
('admin', 'criar_usuario', 'Criar novos usuários no sistema'),
('admin', 'editar_usuario', 'Editar dados de usuários existentes'),
('admin', 'excluir_usuario', 'Excluir usuários do sistema'),
('admin', 'visualizar_relatorios', 'Visualizar relatórios do sistema'),
('admin', 'gerenciar_sistema', 'Gerenciar configurações do sistema'),
('admin', 'backup_dados', 'Realizar backup dos dados');

-- Inserir permissões para usuários comuns
INSERT INTO permissoes (funcao, acao, descricao) VALUES
('usuario', 'visualizar_perfil', 'Visualizar próprio perfil'),
('usuario', 'editar_perfil', 'Editar próprio perfil'),
('usuario', 'visualizar_dados', 'Visualizar dados básicos do sistema');
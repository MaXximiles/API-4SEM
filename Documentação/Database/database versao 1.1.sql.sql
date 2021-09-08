/* ***********************************
NOME DO BANCO: endurance
USUARIO: system
SENHA: 12345
************************************ */
/* ULTIMA MODIFICAÇÃO FEITA EM 01/09/2021 19:44
   POR MAXIMILES                     */

/* ********************************* CRIANDO TABELAS ***************************** */
CREATE TABLE usuarios
(
    usuario_id LONG, 
    usuario_nome VARCHAR(30),
    usuario_cpf VARCHAR(20),
    usuario_rg VARCHAR(20),
    usuario_email VARCHAR(35),
    usuario_senha VARCHAR(255),
    usuario_tipo INTEGER
);
    
CREATE TABLE tipo_usuario
(
    tipo_id LONG,
    tipo_nome VARCHAR(30)
);

CREATE TABLE admin
(
    usuario_id LONG,
    tipo_usuario INTEGER,
    admin_setor VARCHAR(30),
    admin_funcao VARCHAR(30)
);
    
CREATE TABLE colaborador_oracle
(
    usuario_id LONG,
    tipo_usuario INTEGER,
    colaborador_setor VARCHAR(30),
    colaborador_funcao VARCHAR(30),
    colaborador_tipo INTEGER
);
    
CREATE TABLE tipo_oracle
(
    oracle_id LONG,
    oracle_nome VARCHAR(30),
    oracle_prioridade INTEGER
);
    
CREATE TABLE convidado
(
    usuario_id LONG,
    tipo_usuario INTEGER,
    convidado_endereco VARCHAR(30),
    convidado_bairro VARCHAR(30),
    convidado_cep VARCHAR(25),
    convidado_cidade VARCHAR(30),
    convidado_estado CHAR,
    convidado_pais VARCHAR(25)
);

CREATE TABLE convidado_vacina
(
    vacina_id LONG,
    usuario_id INTEGER,
    vacina_arquivo VARCHAR(255),
    vacina_data_upload DATE,
    vacina_hora_upload VARCHAR(10),
    vacina_descricao VARCHAR(30)
);

CREATE TABLE eventos
(
    evento_id LONG,
    evento_data DATE,
    evento_hora_inicio DATE,
    evento_hora_fim VARCHAR(10),
    evento_local VARCHAR(30),
    evento_assunto VARCHAR(30),
    evento_descricao VARCHAR(30),
    evento_observacoes VARCHAR(30),
    usuario_id INTEGER,
    evento_data_criacao DATE,
    evento_hora_criacao VARCHAR(10),
    evento_status VARCHAR(15)
);

CREATE TABLE eventos_status
(
    status_id LONG,
    status_descricao VARCHAR(30)
);

CREATE TABLE participantes
(
	evento_id LONG,
	convidado_id INTEGER
);

/* FIM DAS TABELAS */
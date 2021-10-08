create table usuarios
(
       usr_id number(19,0) generated as identity,
        usr_autoridades raw(255),
        usr_cpf varchar2(255 char) not null,
        usr_email varchar2(255 char) not null,
        usr_nome varchar2(255 char) not null,
        usr_ativo number(1,0),
        usr_bloqueado number(1,0),
        usr_data_cadastro timestamp,
        usr_ultimo_acesso timestamp,
        usr_ultimo_acesso_display timestamp,
        usr_sobrenome varchar2(255 char) not null,
        usr_senha varchar2(255 char) not null,
        usr_image varchar2(255 char),
        usr_tipo varchar2(255 char),
        primary key (usr_id)
);
create table eventos
(
       evt_id number(19,0) not null,
        evt_criacao timestamp,
        evt_desc varchar2(255 char),
        evt_fim timestamp not null,
        evt_inicio timestamp not null,
        evt_local varchar2(255 char) not null,
        evt_max_part number(10,0) not null,
        evt_obs varchar2(255 char),
        evt_status varchar2(255 char) not null,
        evt_tema varchar2(255 char),
        evt_total_part number(10,0) not null,
        evt_usr_id number(19,0),
        primary key (evt_id)
 );
CREATE SCHEMA votar_pauta;

create table votar_pauta.pauta (
    pa_id bigint PRIMARY KEY,
    pa_nome character varying(255) not null,
    pa_status character varying(64) not null,
    pa_votos_sim integer,
    pa_votos_nao integer,
    pa_abertura timestamp(6),
    pa_fechamento timestamp(6)
);

create table votar_pauta.voto (
    vo_id bigint PRIMARY KEY,
    vo_cpf character varying(255) not null,
    vo_valor character varying(64) not null,
    pa_id bigint,
    CONSTRAINT fk_pauta FOREIGN KEY(pa_id)REFERENCES votar_pauta.pauta(pa_id)
);

CREATE SEQUENCE votar_pauta.seq_pauta
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE votar_pauta.seq_voto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
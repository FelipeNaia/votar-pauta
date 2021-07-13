CREATE SCHEMA votar_pauta;

create table votar_pauta.pauta (
    id serial PRIMARY KEY,
    nome character varying(255) not null,
    status character varying(64) not null,
    votos_sim integer,
    votos_nao integer,
    abertura timestamp(6),
    fechamento timestamp(6)
);

create table votar_pauta.voto (
    id serial PRIMARY KEY,
    cpf character varying(255) not null,
    valor character varying(64) not null,
    pauta bigint,
    CONSTRAINT fk_pauta FOREIGN KEY(pauta)REFERENCES votar_pauta.pauta(id)
);
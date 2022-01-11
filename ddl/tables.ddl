CREATE SEQUENCE roles_id_seq start 1;

create table roles
(
    id integer default nextval('roles_id_seq'::regclass) not null
        constraint roles_pk
            primary key,
    name varchar not null
);

alter table roles owner to postgres;

create unique index roles_name_uindex
    on roles (name);

create table users
(
    id bigserial
        constraint users_pk
            primary key,
    email varchar,
    role_id integer not null
        constraint users_roles_id_fk
            references roles,
    password varchar,
    name varchar,
    surname varchar,
    phone varchar(13)
);

alter table users owner to postgres;

create unique index users_email_uindex
    on users (email);

create unique index users_phone_uindex
    on users (phone);

create table cities
(
    id bigserial
        constraint cities_pk
            primary key,
    name varchar not null,
    region varchar,
    locale_key varchar
);

alter table cities owner to postgres;

create unique index cities_name_uindex
    on cities (name);

create table cargoes
(
    id bigserial
        constraint cargoes_pk
            primary key,
    description varchar,
    weight integer not null,
    length integer not null,
    width integer not null,
    height integer not null
);

alter table cargoes owner to postgres;

create table delivery_status
(
    id serial
        constraint delivery_status_pk
            primary key,
    name varchar not null
);

alter table delivery_status owner to postgres;

create table deliveries
(
    id bigserial
        constraint deliveries_pk
            primary key,
    whence varchar not null,
    whither varchar not null,
    create_date date default now() not null,
    delivery_date date,
    distance double precision,
    price integer,
    cargo_id bigint not null
        constraint deliveries_cargoes_id_fk
            references cargoes
            on delete cascade,
    status_id integer default 1 not null
        constraint deliveries_delivery_status_id_fk
            references delivery_status,
    user_id bigint
        constraint deliveries_users_id_fk
            references users,
    to_name varchar,
    from_name varchar,
    from_region_id bigint
        constraint deliveries_cities_id_fk_2
            references cities,
    to_region_id bigint
        constraint deliveries_cities_id_fk
            references cities
);

alter table deliveries owner to postgres;

create unique index delivery_status_name_uindex
    on delivery_status (name);


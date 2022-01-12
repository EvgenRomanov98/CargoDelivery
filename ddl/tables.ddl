CREATE SEQUENCE cargo_roles_id_seq start 1;

create table cargo_roles
(
    id integer default nextval('cargo_roles_id_seq'::regclass) not null
        constraint cargo_roles_pk
            primary key,
    name varchar not null
);

create unique index cargo_roles_name_uindex
    on cargo_roles (name);

create table cargo_users
(
    id bigserial
        constraint cargo_users_pk
            primary key,
    email varchar,
    role_id integer not null
        constraint cargo_users_roles_id_fk
            references cargo_roles,
    password varchar,
    name varchar,
    surname varchar,
    phone varchar(13)
);

create unique index cargo_users_email_uindex
    on cargo_users (email);

create unique index cargo_users_phone_uindex
    on cargo_users (phone);

create table cargo_cities
(
    id bigserial
        constraint cargo_cities_pk
            primary key,
    name varchar not null,
    region varchar,
    locale_key varchar
);

create unique index cargo_cities_name_uindex
    on cargo_cities (name);

create table cargo_cargoes
(
    id bigserial
        constraint cargo_cargoes_pk
            primary key,
    description varchar,
    weight integer not null,
    length integer not null,
    width integer not null,
    height integer not null
);

create table cargo_delivery_status
(
    id serial
        constraint cargo_delivery_status_pk
            primary key,
    name varchar not null
);

create table cargo_deliveries
(
    id bigserial
        constraint cargo_deliveries_pk
            primary key,
    whence varchar not null,
    whither varchar not null,
    create_date date default now() not null,
    delivery_date date,
    distance double precision,
    price integer,
    cargo_id bigint not null
        constraint cargo_deliveries_cargoes_id_fk
            references cargo_cargoes
            on delete cascade,
    status_id integer default 1 not null
        constraint cargo_deliveries_delivery_status_id_fk
            references cargo_delivery_status,
    user_id bigint
        constraint cargo_deliveries_users_id_fk
            references cargo_users,
    to_name varchar,
    from_name varchar,
    from_region_id bigint
        constraint cargo_deliveries_cities_id_fk_2
            references cargo_cities,
    to_region_id bigint
        constraint cargo_deliveries_cities_id_fk
            references cargo_cities
);

create unique index cargo_delivery_status_name_uindex
    on cargo_delivery_status (name);
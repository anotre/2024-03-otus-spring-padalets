create table if not exists genres_ids (
    id bigserial,
    nosql_id varchar(24) unique,
    rdb_id bigint unique,
    primary key (id)
);

create table if not exists authors_ids (
    id bigserial,
    nosql_id varchar(24) unique,
    rdb_id bigint unique,
    primary key (id)
);

create table if not exists books_ids (
    id bigserial,
    nosql_id varchar(24) unique,
    rdb_id bigint unique,
    primary key (id)
);

create table if not exists comments_ids (
    id bigserial,
    nosql_id varchar(24) unique,
    rdb_id bigint unique,
    primary key (id)
);
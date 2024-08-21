create table users (
    id                          bigserial                  primary key,
    username                    varchar_ignorecase(50)     not null unique,
	password                    varchar_ignorecase(60)     not null,
	enabled                     boolean                    not null,
    account_non_expired         boolean                    not null,
    account_non_locked          boolean                    not null,
    credentials_non_expired     boolean                    not null
);

create table authorities (
    id                          bigserial                  primary key,
	authority                   varchar_ignorecase(50)     not null unique
);

create table user_authority (
    id                          bigserial                   primary key,
    user_id                     int8                        not null,
    authority_id                int8                        not null,
    constraint fk_user_id foreign key(user_id) references users(id),
    constraint fk_authority_id foreign key(authority_id) references authorities(id)
)

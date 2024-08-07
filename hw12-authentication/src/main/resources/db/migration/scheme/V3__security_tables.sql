create table users (
    username                    varchar_ignorecase(50)     not null primary key,
	password                    varchar_ignorecase(60)     not null,
	enabled                     boolean                    not null,
    account_non_expired         boolean                    not null,
    account_non_locked          boolean                    not null,
    credentials_non_expired     boolean                    not null
);

create table authorities (
    id          bigserial              primary key,
	username    varchar_ignorecase(50) not null,
	authority   varchar_ignorecase(50) not null,
	constraint  fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);

create table comments (
    id bigserial,
    text varchar(1024),
    book_id bigint references books (id) on delete cascade,
    primary key (id)
);
insert into users(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)
values ('user1', '$2a$04$st2Nh1u.bNobaerc5ySQeO.tGIyZOWH8On6dVL5kmgOXcwx6hmHT6', true, true, true, true),
       ('user2', '$2a$04$st2Nh1u.bNobaerc5ySQeO.tGIyZOWH8On6dVL5kmgOXcwx6hmHT6', false, true, false, true);

insert into authorities(username, authority)
values ('user1', 'ROLE_ADMIN'), ('user2', 'ROLE_USER');
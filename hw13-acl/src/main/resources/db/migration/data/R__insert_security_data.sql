insert into users(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)
values ('admin', '$2a$04$st2Nh1u.bNobaerc5ySQeO.tGIyZOWH8On6dVL5kmgOXcwx6hmHT6', true, true, true, true),
       ('userBlocked', '$2a$04$st2Nh1u.bNobaerc5ySQeO.tGIyZOWH8On6dVL5kmgOXcwx6hmHT6', false, true, false, true),
       ('user', '$2a$04$st2Nh1u.bNobaerc5ySQeO.tGIyZOWH8On6dVL5kmgOXcwx6hmHT6', true, true, true, true),
       ('userForbidden', '$2a$04$st2Nh1u.bNobaerc5ySQeO.tGIyZOWH8On6dVL5kmgOXcwx6hmHT6', true, true, true, true);

insert into authorities(authority)
values ('ROLE_ADMIN'), ('ROLE_USER'), ('RULE_FORBIDDEN');

insert into user_authority(user_id, authority_id)
values (1, 1), (2, 2), (3, 2), (4, 3);
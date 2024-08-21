insert into users(id, username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)
values (1, 'admin', '$2a$04$st2Nh1u.bNobaerc5ySQeO.tGIyZOWH8On6dVL5kmgOXcwx6hmHT6', true, true, true, true),
       (2, 'userBlocked', '$2a$04$st2Nh1u.bNobaerc5ySQeO.tGIyZOWH8On6dVL5kmgOXcwx6hmHT6', false, true, false, true),
       (3, 'user', '$2a$04$st2Nh1u.bNobaerc5ySQeO.tGIyZOWH8On6dVL5kmgOXcwx6hmHT6', true, true, true, true),
       (4, 'userForbidden', '$2a$04$st2Nh1u.bNobaerc5ySQeO.tGIyZOWH8On6dVL5kmgOXcwx6hmHT6', true, true, true, true);

insert into authorities(id, authority)
values (1, 'ROLE_ADMIN'), (2, 'ROLE_USER'), (3, 'RULE_FORBIDDEN');

insert into user_authority(user_id, authority_id)
values (1, 1), (2, 2), (3, 2), (4, 3);
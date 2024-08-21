insert into acl_class(id, class)
values (1, 'ru.otus.hw.models.Book');

insert into acl_sid(id, principal, sid)
values (1, true, 'admin'),
       (2, true, 'userBlocked'),
       (3, true, 'user'),
       (4, false, 'ROLE_ADMIN');

insert into acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
values (1, 1, 1, null, 1, true),
       (2, 1, 2, null, 1, true),
       (3, 1, 3, null, 1, true);

insert into acl_entry (id, acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) values
-- права ROLE_ADMIN для book c id = 1 на read, write, create и delete
(1, 1, 1, 4, 1, 1, 1, 1),
(2, 1, 2, 4, 2, 1, 1, 1),
(3, 1, 3, 4, 4, 1, 1, 1),
(4, 1, 4, 4, 8, 1, 1, 1),
-- права ROLE_ADMIN для book c id = 2 на read, write, create и delete
(5, 2, 1, 4, 1, 1, 1, 1),
(6, 2, 2, 4, 2, 1, 1, 1),
(7, 2, 3, 4, 4, 1, 1, 1),
(8, 2, 4, 4, 8, 1, 1, 1),
-- права ROLE_ADMIN для book c id = 3 на read, write, create и delete
(9, 3, 1, 4, 1, 1, 1, 1),
(10, 3, 2, 4, 2, 1, 1, 1),
(11, 3, 3, 4, 4, 1, 1, 1),
(12, 3, 4, 4, 8, 1, 1, 1),
---- права user3 для book c id = 1 на read
--(13, 1, 5, 3, 1, 1, 1, 1),
---- права user3 для book c id = 2 на read
--(14, 2, 5, 3, 1, 1, 1, 1),
-- права user3 для book c id = 3 на read, write, create и delete
(15, 3, 5, 3, 1, 1, 1, 1),
(16, 3, 6, 3, 2, 1, 1, 1),
(17, 3, 7, 3, 4, 1, 1, 1),
(18, 3, 8, 3, 8, 1, 1, 1);

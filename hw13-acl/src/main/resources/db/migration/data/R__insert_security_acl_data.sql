insert into acl_class(class)
values ('ru.otus.hw.models.Book');

insert into acl_sid(principal, sid)
values (true, 'admin'),
       (true, 'userBlocked'),
       (true, 'user'),
       (false, 'ROLE_ADMIN');

insert into acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
values (1, 1, null, 1, true),
       (1, 2, null, 1, true),
       (1, 3, null, 1, true);

insert into acl_entry (acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) values
-- права ROLE_ADMIN для book c id = 1 на read, write, create и delete
(1, 1, 4, 1, 1, 1, 1),
(1, 2, 4, 2, 1, 1, 1),
(1, 3, 4, 4, 1, 1, 1),
(1, 4, 4, 8, 1, 1, 1),
-- права ROLE_ADMIN для book c id = 2 на read, write, create и delete
(2, 1, 4, 1, 1, 1, 1),
(2, 2, 4, 2, 1, 1, 1),
(2, 3, 4, 4, 1, 1, 1),
(2, 4, 4, 8, 1, 1, 1),
-- права ROLE_ADMIN для book c id = 3 на read, write, create и delete
(3, 1, 4, 1, 1, 1, 1),
(3, 2, 4, 2, 1, 1, 1),
(3, 3, 4, 4, 1, 1, 1),
(3, 4, 4, 8, 1, 1, 1),
---- права user3 для book c id = 1 на read
--(13, 1, 5, 3, 1, 1, 1, 1),
---- права user3 для book c id = 2 на read
--(14, 2, 5, 3, 1, 1, 1, 1),
-- права user3 для book c id = 3 на read, write, create и delete
(3, 5, 3, 1, 1, 1, 1),
(3, 6, 3, 2, 1, 1, 1),
(3, 7, 3, 4, 1, 1, 1),
(3, 8, 3, 8, 1, 1, 1);

package ru.otus.hw.services.security;

import java.io.Serializable;

public interface AclService<T> {
    void createAclFor(T object);

    void deleteAclByObjectId(Serializable id);
}

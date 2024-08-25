package ru.otus.hw.services.security;

import java.io.Serializable;

public interface AclService {
    void createAclFor(Object object);

    void deleteAclByObjectId(Serializable id);
}

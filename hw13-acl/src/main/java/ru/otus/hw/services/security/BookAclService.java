package ru.otus.hw.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;

import java.io.Serializable;

@Service
@RequiredArgsConstructor
public class BookAclService implements AclService {
    private final MutableAclService aclService;

    @Override
    @Transactional
    public void createAclFor(Object object) {
        var oid = new ObjectIdentityImpl(object);
        var acl = this.aclService.createAcl(oid);
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var startSize = acl.getEntries().size();
        acl.insertAce(startSize, BasePermission.READ, new PrincipalSid(authentication), true);
        acl.insertAce(startSize + 1, BasePermission.WRITE, new PrincipalSid(authentication), true);
        acl.insertAce(startSize + 2, BasePermission.DELETE, new PrincipalSid(authentication), true);
        this.aclService.updateAcl(acl);
    }

    @Override
    public void deleteAclByObjectId(Serializable id) {
        aclService.deleteAcl(new ObjectIdentityImpl(Book.class, id), true);
    }
}

package ru.otus.hw.service;

import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AclServiceWrapperServiceImpl implements AclServiceWrapperService {

    private final MutableAclService mutableAclService;

    public AclServiceWrapperServiceImpl(MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
    }

    @Override
    public void createPermission(Object object, Permission permission) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        final var owner = new PrincipalSid(authentication);
        var oid = new ObjectIdentityImpl(object);
        final var admin = new GrantedAuthoritySid("ROLE_ADMIN");
        var acl = mutableAclService.createAcl(oid);
        acl.insertAce(acl.getEntries().size(), permission, owner, true);
        acl.insertAce(acl.getEntries().size(), permission, admin, true);
        mutableAclService.updateAcl(acl);
    }
}

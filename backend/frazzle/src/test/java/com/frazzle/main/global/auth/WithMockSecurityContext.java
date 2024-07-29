package com.frazzle.main.global.auth;

import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.global.models.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockSecurityContext implements WithSecurityContextFactory<WithMockAuthUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockAuthUser mockUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        User user = User.createUser("1", "김싸피", mockUser.email(), "kakao");
        UserPrincipal memberDetails = UserPrincipal.create(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
        context.setAuthentication(authentication);
        return context;
    }
}
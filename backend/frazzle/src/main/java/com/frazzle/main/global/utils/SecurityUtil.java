package com.frazzle.main.global.utils;

import com.frazzle.main.domain.auth.models.UserPrincipal;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

//securityContext authentication 에서 유저정보의 userId만 가져오는 메서드
public class SecurityUtil {

    public SecurityUtil() {
    }

    public static long getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        long userId;
        if(authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            userId = userPrincipal.getId();
        }
        else {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        return userId;
    }
}

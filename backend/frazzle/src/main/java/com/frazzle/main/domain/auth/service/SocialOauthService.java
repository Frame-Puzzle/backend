package com.frazzle.main.domain.auth.service;

import com.frazzle.main.domain.user.entity.User;

import java.util.Map;

public interface SocialOauthService {

    Map<String, Object> getUserAttributesByToken(String accessToken);

    User getUserProfileByToken(String accessToken);
}

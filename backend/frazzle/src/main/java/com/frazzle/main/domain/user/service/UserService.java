package com.frazzle.main.domain.user.service;

import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    //UserId로 유저 찾기
    public User findByLoginUserId(String id) {
        return userRepository.findByLoginUserId(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public int update(User user) {
        return userRepository.updateUser(user);
    }

    public int updateRefreshToken(User user) {
        return userRepository.updateRefreshToken(user);
    }

    public User findByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken);
    }


}

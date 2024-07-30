package com.frazzle.main.domain.user.service;

import com.frazzle.main.domain.user.dto.UpdateUserNicknameRequestDto;
import com.frazzle.main.domain.user.dto.UpdateUserProfileRequestDto;
import com.frazzle.main.domain.user.dto.UpdateUserRequestDto;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.entity.UserDirectory;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserDirectoryRepository userDirectoryRepository;

    //UserId로 유저 찾기
//    public User findByLoginUserId(String id) {
//        return userRepository.findByLoginUserId(id);
//    }

    //insert문
    public User save(User user) {
        return userRepository.save(user);
    }

    //주어진 정보로 업데이트
    @Transactional
    public Long updateUser(User findUser, User inputUser) {
        return userRepository.updateUser(findUser, inputUser);
    }

    //유저id로 유저 찾기
    public User findByUserId(int id) {
        return userRepository.findByUserId(id);
    }

    @Transactional
    public User updateUserByNickname(User user, UpdateUserNicknameRequestDto requestDto) {
        user.updateUserNickname(requestDto.getNickname());
        return userRepository.save(user);
    }

    @Transactional
    public User updateUserByProfileImg(User user, String url) {
        user.updateUserProfileImg(url);
        return userRepository.save(user);
    }

    //닉네임 존재 여부 체크
    public Boolean findByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    //사용자 삭제
    @Transactional
    public Long deleteUser(int userId) {
        User user = userRepository.findByUserId(userId);

        //유저가 있으면
        if(user != null) {
            userDirectoryRepository.deleteByUser(user);
            return userRepository.deleteByUserId(userId);
        }

        throw new CustomException(ErrorCode.NOT_EXIST_USER);
    }

    //리프레시 토큰 업데이트
    @Transactional
    public Long updateRefreshToken(User user, String refreshToken) {
        return userRepository.updateRefreshToken(user, refreshToken);
    }

    //리프레시 토큰으로 사용자 찾기
    public User findByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken);
    }


}

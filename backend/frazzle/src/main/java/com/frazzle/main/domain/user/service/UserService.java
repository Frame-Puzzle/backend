package com.frazzle.main.domain.user.service;

import com.frazzle.main.domain.user.dto.UpdateUserRequestDto;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    //UserId로 유저 찾기
//    public User findByLoginUserId(String id) {
//        return userRepository.findByLoginUserId(id);
//    }

    //insert문
    public User save(User user) {
        return userRepository.save(user);
    }

    //주어진 정보로 업데이트
    public Long upateUser(User findUser, User inputUser) {
        return userRepository.updateUser(findUser, inputUser);
    }

    //유저id로 유저 찾기
    public User findByUserId(int id) {
        return userRepository.findByUserId(id);
    }

    public Long updateUserByNicknameOrImg(User user, UpdateUserRequestDto requestDto) {

        Long result = null;

        //만약 닉네임 변경시 여기서 발생
        if(requestDto.getNickname() != null && !requestDto.getNickname().isBlank()) {
            result = userRepository.updateNickname(user, requestDto);
        }

        //만약 프로필 사진만 변경시 여기서 발생
        else if(requestDto.getProfileImg() != null && !requestDto.getProfileImg().isBlank()) {
            result = userRepository.updateProfileImg(user, requestDto);
        }

        return result;
    }

    //닉네임 존재 여부 체크
    public Boolean findByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    //사용자 삭제
    @Transactional
    public Long deleteUser(int userId) {
        return userRepository.deleteByUserId(userId);
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

package com.frazzle.main.domain.user.service;

import com.frazzle.main.domain.user.dto.UpdateUserNicknameRequestDto;
import com.frazzle.main.domain.user.dto.UpdateUserProfileRequestDto;
import com.frazzle.main.domain.user.dto.UpdateUserRequestDto;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.entity.UserDirectory;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import com.frazzle.main.global.aws.service.AwsService;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.models.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserDirectoryRepository userDirectoryRepository;
    private final AwsService awsService;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //insert문
    public User save(User user) {
        return userRepository.save(user);
    }

    //유저id로 유저 찾기
    public User findUser(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    //주어진 정보로 업데이트
    @Transactional
    public Long updateUser(User findUser, User inputUser) {
        return userRepository.updateUser(findUser, inputUser);
    }

    //유저id로 유저 찾기
    public User findByUserId(UserPrincipal userPrincipal) {
        return userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );
    }

    @Transactional
    public User updateUserByNickname(UserPrincipal userPrincipal, UpdateUserNicknameRequestDto requestDto) {
        //1. 유저 정보 확인
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        if(findByNickname(requestDto.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
        }

        user.updateUserNickname(requestDto.getNickname());
        return userRepository.save(user);
    }

    @Transactional
    public User updateUserByProfileImg(UserPrincipal userPrincipal, MultipartFile profileImg) {
        //1. 유저 정보 확인
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        //이미 프로필 사진 존재하면 삭제
        if(user.getProfileImg()!=null) {
            awsService.deleteProfile(user.getProfileImg());
        }

        //사진 업로드 후 고유url 반환
        String imgUrl = awsService.uploadFile(profileImg);

        //유저url을 통해 S3에서 이미지 가져오기
        String url = awsService.getProfileUrl(imgUrl);

        user.updateUserProfileImg(url);
        return userRepository.save(user);
    }

    //닉네임 존재 여부 체크
    public Boolean findByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    //사용자 삭제
    @Transactional
    public Long deleteUser(UserPrincipal userPrincipal) {
        //1. 유저 정보 확인
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        //유저가 있으면
        if(user != null) {
            userDirectoryRepository.deleteByUser(user);
            return userRepository.deleteByUserId(user.getUserId());
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
        return userRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );
    }


}

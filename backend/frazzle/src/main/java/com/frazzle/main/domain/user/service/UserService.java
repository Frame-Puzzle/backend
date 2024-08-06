package com.frazzle.main.domain.user.service;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.service.DirectoryService;
import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.notification.repository.NotificationRepository;
import com.frazzle.main.domain.piece.entity.Piece;
import com.frazzle.main.domain.piece.repository.PieceRepository;
import com.frazzle.main.domain.piece.service.PieceService;
import com.frazzle.main.domain.user.dto.UpdateUserNicknameRequestDto;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import com.frazzle.main.domain.usernotification.entity.UserNotification;
import com.frazzle.main.domain.usernotification.repository.UserNotificationRepository;
import com.frazzle.main.global.aws.service.AwsService;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.models.UserPrincipal;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserDirectoryRepository userDirectoryRepository;
    private final AwsService awsService;
    private final DirectoryService directoryService;
    private final UserNotificationRepository userNotificationRepository;
    private final PieceRepository pieceRepository;
    private final BoardRepository boardRepository;
    private final NotificationRepository notificationRepository;
    private final EntityManager em;

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
        return userPrincipal.getUser();
    }

    @Transactional
    public User updateUserByNickname(UserPrincipal userPrincipal, UpdateUserNicknameRequestDto requestDto) {
        //1. 유저 정보 확인
        User user = userPrincipal.getUser();

        if(findByNickname(requestDto.getNickname())) {
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
        }

        user.updateUserNickname(requestDto.getNickname());
        return userRepository.save(user);
    }

    @Transactional
    public User updateUserByProfileImg(UserPrincipal userPrincipal, MultipartFile profileImg) {
        //1. 유저 정보 확인
        User user = userPrincipal.getUser();

        //이미 프로필 사진 존재하면 삭제
        if(user.getProfileImg()!=null) {
            awsService.deleteImage(user.getProfileImg());
        }

        //사진 업로드 후 고유url 반환
        String imgUrl = awsService.uploadFile(profileImg);

        //유저url을 통해 S3에서 이미지 가져오기
        String url = awsService.getImageUrl(imgUrl);

        user.updateUserProfileImg(url);
        return userRepository.save(user);
    }

    //닉네임 존재 여부 체크
    public Boolean findByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    //사용자 삭제
    @Transactional
    public void deleteUser(UserPrincipal userPrincipal) {
        //1. 유저 정보 확인
        User user = userPrincipal.getUser();

        //2. 유저 디렉토리에서 유저가 가입한 디렉토리 리스트 찾기
        List<Integer> directoryId = userDirectoryRepository.findDirectoryIdByUserAndIsAccept(user, true);

        //3. 반복문으로 디렉토리 탈퇴
        for(int id : directoryId) {
            directoryService.leaveDirectory(userPrincipal, id);
        }

        List<UserNotification> userNotificationList = userNotificationRepository.findByUser(user);
        if(!userNotificationList.isEmpty()) {
            userNotificationRepository.deleteAll(userNotificationList);
        }

            //4. 유저 디렉토리 삭제
        userDirectoryRepository.deleteByUser(user);

        //4. 유저 삭제
        userRepository.deleteByUserId(user.getUserId());
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

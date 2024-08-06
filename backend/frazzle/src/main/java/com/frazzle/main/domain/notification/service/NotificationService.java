package com.frazzle.main.domain.notification.service;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.notification.dto.AcceptNotificationRequestDto;
import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.notification.repository.NotificationRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.entity.UserDirectory;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import com.frazzle.main.domain.usernotification.entity.UserNotification;
import com.frazzle.main.domain.usernotification.repository.UserNotificationRepository;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.models.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final UserRepository userRepository;
    private final UserDirectoryRepository userDirectoryRepository;

    @Transactional
    public List<UserNotification> findAllByUser(UserPrincipal userPrincipal) {
        //1. 유저 정보 확인
        User user = userPrincipal.getUser();

        return userNotificationRepository.findByUser(user);
    }

    @Transactional
    public void updateUserNotification(UserPrincipal userPrincipal, int notificationId, AcceptNotificationRequestDto requestDto) {
        //1. 유저 정보 확인
        User user = userPrincipal.getUser();

        Notification notification = notificationRepository.findByNotificationId(notificationId);

        UserNotification userNotification = userNotificationRepository.findByUserAndNotification(user, notification).orElseThrow(
                () -> new CustomException(ErrorCode.UNAUTHORIZED)
        );

        userNotification.updateRead();

        userNotification.updateStatus(requestDto.getAccept());

    }

    @Transactional
    public void createNotificationWithInviteDirectory(String keyword, int type, User user, User inviteMember, Directory directory) {
        //알림 생성
        Notification requestNotification = Notification.createNotificationWithDirectory(keyword, type, user, directory);

        //알림 저장
        Notification notification =  notificationRepository.save(requestNotification);

        UserNotification userNotification = UserNotification.createUserNotification(inviteMember, notification);

        //초대된 사람의 알림 저장
        userNotificationRepository.save(userNotification);

    }

    @Transactional
    public void createNotificationWithBoard(String keyword, int type, User user, Board board) {

        Directory directory = board.getDirectory();
        //알림 생성
        Notification requestNotification = Notification.createNotificationWithBoard(keyword, type, user, directory, board);

        //알림 저장
        Notification notification =  notificationRepository.save(requestNotification);

        //디렉토리의 참여한 유저들 찾기
        List<UserDirectory> userDirectoryList = userDirectoryRepository.findByDirectoryAndIsAccept(directory, true);

        List<UserNotification> userNotificationList = new ArrayList<>();

        //유저 알림 저장
        for(UserDirectory userDirectory: userDirectoryList) {
            User groupUser = userDirectory.getUser();
            userNotificationList.add(UserNotification.createUserNotification(groupUser, notification));

        }
        //디렉토리에 있는 유저들 모두에게 알림 저장
        userNotificationRepository.saveAll(userNotificationList);
    }
}

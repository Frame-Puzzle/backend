package com.frazzle.main.domain.notification.service;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.board.service.BoardService;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
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
    private final BoardService boardService;
    private final DirectoryRepository directoryRepository;
    private final BoardRepository boardRepository;

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

        //알림 타입 0은 디렉토리 초대
        //accept 상태는 1은 수락 2는 거절
        if(notification.getType()==0) {
            Directory directory = notification.getDirectory();
            if(requestDto.getAccept()==1) {
                UserDirectory userDirectory = userDirectoryRepository.findByUserAndDirectory(user, directory);
                userDirectory.updateAccept(true);
            }

            //거절 시
            if(requestDto.getAccept()==2) {
                userDirectoryRepository.deleteByUserAndDirectory(user, directory);
                directory.changePeopleNumber(-1);
            }
        }

        //알림 타입 1은 투표 하기
        if(notification.getType()==1) {
            Board board = notification.getBoard();
            //1은 삭제 수락
            if(requestDto.getAccept()==1) {
                updateVote(board, true);
            }
            
            //2는 삭제 거절
            if(requestDto.getAccept()==2) {
                updateVote(board, false);
            }
        }


    }

    @Transactional
    public void updateVote(Board board, Boolean vote) {
        //퍼즐판 투표가 비활성화 되면
        if(!board.isVote()) {
            throw new CustomException(ErrorCode.VOTE_NOT_FOUND);
        }
        //투표 수락
        if(vote) {
            board.addVoteNumber();
            boardRepository.save(board);
            if (board.getVoteNumber() >= board.getDirectory().getPeopleNumber()) {
                boardService.deleteBoard(board);
            }
        }

        if(!vote) {
            board.changeVote();
            board.changeVoteNumber(0);
            boardRepository.save(board);
        }
        
    }

}
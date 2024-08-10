package com.frazzle.main.domain.socket.roby.service;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.board.service.BoardService;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.notification.entity.NotificationTypeFlag;
import com.frazzle.main.domain.notification.repository.NotificationRepository;
import com.frazzle.main.domain.socket.roby.entity.Roby;
import com.frazzle.main.domain.socket.roby.entity.RobyUser;
import com.frazzle.main.domain.socket.roby.listener.RobyEventListener;
import com.frazzle.main.domain.piece.entity.Piece;
import com.frazzle.main.domain.piece.repository.PieceRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.entity.UserDirectory;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import com.frazzle.main.domain.usernotification.entity.UserNotification;
import com.frazzle.main.domain.usernotification.repository.UserNotificationRepository;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RobyService {
    private final UserRepository userRepository;
    private final Map<Integer, Roby> robyList = new HashMap<>();
    private final List<RobyEventListener> listeners = new ArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final BoardRepository boardRepository;
    private final DirectoryRepository directoryRepository;
    private final PieceRepository pieceRepository;
    private final BoardService boardService;
    private final NotificationRepository notificationRepository;
    private final UserDirectoryRepository userDirectoryRepository;
    private final UserNotificationRepository userNotificationRepository;

    @Transactional
    public void createRoby(int boardId, RobyUser robyUser) {
        if (!robyList.containsKey(boardId)) {
            User user = userRepository.findByUserId(robyUser.getUserId()).orElseThrow(
                    () -> new CustomException(ErrorCode.NOT_EXIST_USER)
            );
            Directory directory = directoryRepository.findByBoardId(boardId).orElseThrow(
                    () -> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY)
            );
            Board board = boardRepository.findById(boardId).orElseThrow(
                    () -> new CustomException(ErrorCode.NOT_EXIST_BOARD)
            );

            Piece piece = pieceRepository.findByBoardOrderByPeopleCountDesc(board).get(0);

            // 로비 생성
            Roby roby = Roby.createRoby(boardId, directory.getPeopleNumber(), piece.getImageUrl());
            robyList.put(boardId, roby);

            long delay = roby.getEndTime().getTime() - System.currentTimeMillis();
            scheduler.schedule(() -> removeRoby(boardId), delay, TimeUnit.MILLISECONDS);

            boardService.createNotificationWithBoard(board.getDirectory().getCategory(), NotificationTypeFlag.CREATE_GAME_ROOM.getValue(), user, board);

            log.info("Roby created for boardId={}", boardId);
        }
    }

    private void removeRoby(int robyId) {
        Roby roby = robyList.remove(robyId);
        if (roby != null) {
            for (RobyUser user : roby.getRobyUserList()) {
                notifyUserRemoved(roby, user);
            }
        }
    }

    @Transactional
    public void addUserToRoby(int boardId, RobyUser robyUser) {
        Roby roby = robyList.get(boardId);

        if (roby == null) {
            createRoby(boardId, robyUser);
            roby = robyList.get(boardId);
            if (roby != null) {
                roby.updateUser(robyUser);
            }
        }

        boolean userExists = roby.getRobyUserList().stream()
                .anyMatch(existingUser -> existingUser.getNickname().equals(robyUser.getNickname()));

        if (!userExists) {
            notifyUserAdded(roby, robyUser);
            roby.addRobyUser(robyUser);
        }
    }

    public void removeUserFromRoby(int robyId, RobyUser inputUser) {
        Roby roby = robyList.get(robyId);
        if (roby != null) {
            RobyUser removeUser = roby.getRobyUserList().stream()
                    .filter(user -> inputUser.equals(user))
                    .findFirst().orElse(null);

            if (removeUser != null) {
                roby.getRobyUserList().remove(removeUser);
                notifyUserRemoved(roby, removeUser);

                if (roby.getKing().equals(inputUser)) {
                    if (!roby.getRobyUserList().isEmpty()) {
                        roby.updateUser(roby.getRobyUserList().get(0));
                    }
                }

                if (roby.getRobyUserList().isEmpty()) {
                    robyList.remove(robyId);
                }
            }
        }
    }

    public List<RobyUser> getUsersInRoby(int robyId) {
        Roby roby = robyList.get(robyId);
        if (roby != null) {
            return roby.getRobyUserList();
        }
        return Collections.emptyList();
    }

    public Date getRobyStartTime(int robyId) {
        Roby roby = robyList.get(robyId);
        if (roby != null) {
            return roby.getStartTime();
        }
        return null;
    }

    public Date getRobyEndTime(int robyId) {
        Roby roby = robyList.get(robyId);
        if (roby != null) {
            return roby.getEndTime();
        }
        return null;
    }

    public void addRobyEventListener(RobyEventListener listener) {
        listeners.add(listener);
    }

    public void removeRobyEventListener(RobyEventListener listener) {
        listeners.remove(listener);
    }

    private void notifyUserAdded(Roby roby, RobyUser robyUser) {
        for (RobyEventListener listener : listeners) {
            listener.onUserAdded(roby, robyUser);
        }
    }

    private void notifyUserRemoved(Roby roby, RobyUser user) {
        for (RobyEventListener listener : listeners) {
            listener.onUserRemoved(roby, user);
        }
    }

    public Roby getRoby(int robyId) {
        return robyList.get(robyId);
    }

    public boolean isEmpty(int boardId) {
        Roby roby = robyList.get(boardId);
        return roby == null || roby.getRobyUserList().isEmpty();
    }

    @Transactional
    public void createNotificationWithBoard(String keyword, int type, User user, Board board) {

        Directory directory = board.getDirectory();
        //알림 생성
        Notification notification = Notification.createNotificationWithBoard(keyword, type, user, directory, board);

        //알림 저장
        notificationRepository.save(notification);

        //디렉토리의 참여한 유저들 찾기
        List<UserDirectory> userDirectoryList = userDirectoryRepository.findByDirectoryAndIsAccept(directory, true);

        List<UserNotification> userNotificationList = new ArrayList<>();

        //유저 알림 저장
        for(UserDirectory userDirectory: userDirectoryList) {
            User groupUser = userDirectory.getUser();
            if(groupUser.getUserId() == user.getUserId()) continue;
            userNotificationList.add(UserNotification.createUserNotification(groupUser, notification));
        }
        //디렉토리에 있는 유저들 모두에게 알림 저장
        userNotificationRepository.saveAll(userNotificationList);
    }
}
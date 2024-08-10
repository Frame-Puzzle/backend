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

            Roby roby = Roby.createRoby(boardId, directory.getPeopleNumber(), piece.getImageUrl());
            robyList.put(boardId, roby);
            long delay = roby.getEndTime().getTime() - System.currentTimeMillis();
            scheduler.schedule(() -> removeRoby(boardId), delay, TimeUnit.MILLISECONDS);

            boardService.createNotificationWithBoard(board.getDirectory().getCategory(), NotificationTypeFlag.CREATE_GAME_ROOM.getValue(), user, board);

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

    public void addUserToRoby(int boardId, RobyUser robyUser) {

        Roby roby = robyList.get(boardId);

        if (roby == null) {
            createRoby(boardId, robyUser);
            roby = robyList.get(boardId);
            roby.updateUser(robyUser);
        }

        // 유저 리스트에서 닉네임 비교하여 포함 여부 확인
        boolean userExists = roby.getRobyUserList().stream()
                .anyMatch(existingUser -> existingUser.getNickname().equals(robyUser.getNickname()));

        if (userExists) {
            return;
        }

        //없으면 대기방에 속한 유저들에게 알림 주기
        notifyUserAdded(roby, robyUser);
        roby.addRobyUser(robyUser);
    }

    public void removeUserFromRoby(int robyId, RobyUser inputUser) {

        Roby roby = robyList.get(robyId);
        RobyUser removeUser = null;
        if (roby != null) {
            List<RobyUser> robyUserList = roby.getRobyUserList();
            //유저 닉네임 같은 것 찾기
            for (RobyUser user : robyUserList) {
                if (inputUser.equals(user)) {
                    removeUser = user;
                    break;
                }
            }

            robyUserList.remove(removeUser);
            notifyUserRemoved(roby, removeUser);

            if(roby.getKing().equals(inputUser)) {
                roby.updateUser(roby.getRobyUserList().get(0));
            }

            if (roby.getRobyUserList().isEmpty()) {
                robyList.remove(robyId);
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
}
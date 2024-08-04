package com.frazzle.main.domain.directory.service;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.board.repository.BoardRepository;
import com.frazzle.main.domain.directory.dto.*;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.notification.entity.Notification;
import com.frazzle.main.domain.notification.repository.NotificationRepository;
import com.frazzle.main.domain.piece.repository.PieceRepository;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.entity.UserDirectory;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import com.frazzle.main.domain.usernotification.repository.UserNotificationRepository;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.models.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DirectoryService {

    private final DirectoryRepository directoryRepository;
    private final UserDirectoryRepository userDirectoryRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final PieceRepository pieceRepository;

    @Transactional
    public Directory createDirectory(UserPrincipal userPrincipal, CreateDirectoryRequestDto requestDto) {
        //1. 유저 정보 확인
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        //2. 디렉토리 생성
        Directory directory = Directory.createDirectory(requestDto);
        directoryRepository.save(directory);

        //3. 유저-디렉토리 정보 저장
        UserDirectory userDirectory = UserDirectory.createUserDirectory(directory, user, true);
        userDirectoryRepository.save(userDirectory);

        return directory;
    }

    @Transactional
    public void updateDirectoryName(UserPrincipal userPrincipal, UpdateDirectoryNameRequestDto requestDto, int directoryId){
        //1. 유저 정보 및 디렉토리 정보 확인
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );;
        Directory directory = directoryRepository.findByDirectoryId(directoryId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY));

        //2. 유저가 디렉토리에 가입되어 있지 않으면 에러
        if(!userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user, true)) {
          throw new CustomException(ErrorCode.DENIED_UPDATE);
        }

        //3. 유저가 디렉토리에 가입되어 있으면 수정
        directory.changeDirectoryName(requestDto.getDirectoryName());
    }

    @Transactional
    public List<UserByEmailResponseDto> findUserByEmail(UserPrincipal userPrincipal, String email, int directoryId) {
        //1. 유저 정보 및 디렉토리 정보 확인
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );;
        Directory directory = directoryRepository.findByDirectoryId(directoryId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY));

        //2. 유저가 디렉토리에 가입되어 있지 않으면 에러
        if(!userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user, true)) {
            throw new CustomException(ErrorCode.DENIED_FIND_MEMBER);
        }

        //3. 유저가 디렉토리게 가입되어 있으면 멤버 조회
        List<User> users = userRepository.findUsersByEmail(email, directory);

        List<UserByEmailResponseDto> response = new ArrayList<>();

        for(User u : users) {
            response.add(UserByEmailResponseDto.createFindUserByEmailResponseDto(u));
        }

        return response;
    }

    @Transactional
    public void inviteMember(UserPrincipal userPrincipal, InviteOrCancelMemberRequestDto requestDto, int directoryId) {
        //1. 유저 정보 및 디렉토리 정보 확인
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );
        Directory directory = directoryRepository.findByDirectoryId(directoryId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY)
        );

        //2. 유저가 디렉토리에 가입되어 있지 않으면 에러
        if(!userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user, true)) {
            throw new CustomException(ErrorCode.DENIED_INVITE_MEMBER);
        }

        //3. 초대하려는 유저가 존재하지 않거나 디렉토리에 가입되어 있으면 에러
        User member = userRepository.findByUserId(requestDto.getUserId()).orElseThrow(
                ()->new CustomException(ErrorCode.NOT_EXIST_USER));

        if(userDirectoryRepository.existsByUserAndDirectory(member, directory)) {
            throw new CustomException(ErrorCode.DUPLICATED_DIRECTORY_MEMBER);
        }

        //4. 멤버 초대
        userDirectoryRepository.save(UserDirectory.createUserDirectory(directory, member, false));
        directory.changePeopleNumber(1);
        /*
        fcm 코드
         */
    }

    @Transactional
    public void cancelMemberInvitation(UserPrincipal userPrincipal, InviteOrCancelMemberRequestDto requestDto, int directoryId) {
        //1. 유저 및 디렉토리 확인
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );
        Directory directory = directoryRepository.findByDirectoryId(directoryId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY)
        );
        User member = userRepository.findByUserId(requestDto.getUserId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        //2. 멤버 초대 취소 권한 확인
        //초대하는 사람이 디렉토리에 가입된 멤버가 아니면 에러
        if(!userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user, true)) {
            throw new CustomException(ErrorCode.DENIED_CANCEL_MEMBER);
        }
        //초대 취소되는 사람이 디렉토리에 초대가 되었고 승인하기 전이 아니면 에러
        if(!userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, member, false)){
            throw new CustomException(ErrorCode.DENIED_CANCEL_MEMBER);
        }

        //3. 초대 취소
        userDirectoryRepository.deleteByUserAndDirectory(member, directory);
        directory.changePeopleNumber(-1);
    }

    @Transactional
    public List<FindMyDirectoryResponseDto> findMyDirectory(UserPrincipal userPrincipal, String category){
        //1. 유저 인증
        User user = userRepository.findByUserId(userPrincipal.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        //2. 유저 디렉토리 조회
        List<Directory> directories = directoryRepository.findMyDirectory(user, category);
        List<FindMyDirectoryResponseDto> response = new ArrayList<>();

        for(Directory d : directories) {
            response.add(FindMyDirectoryResponseDto.createFindMyDirectoryResponseDto(d));
        }

        return response;
    }

    @Transactional
    public DetailDirectoryResponsetDto findDetailDirectory(UserPrincipal userPrincipal, int directoryId) {
        int userId = userPrincipal.getId();

        if(!userDirectoryRepository.existsByUser_UserIdAndDirectory_DirectoryIdAndIsAccept(userId, directoryId, true)) {
            throw new CustomException(ErrorCode.DENIED_DIRECTORY);
        }

        Directory directory = directoryRepository.findByDirectoryId(directoryId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY)
        );

        List<User> members = userRepository.findDirectoryUsers(directory);
        List<MemberListDto> memberList = new ArrayList<>();
        for(User m : members) {
            memberList.add(MemberListDto.createMemberList(m));
        }

        List<Board> boards = boardRepository.findBoards(directoryId);
        List<BoardListDto> boardList = new ArrayList<>();
        for(Board b : boards) {
            boardList.add(BoardListDto.createBoardList(b));
        }

        boolean isCurrentBoard = false;
        if(boards.size()>0){
            isCurrentBoard = boards.get(0).getClearType() == 0 ? true : false;
        }

        DetailDirectoryResponsetDto detailDirectoryResponsetDto
                = DetailDirectoryResponsetDto.createDetailDirectoryRequestDto(
                directory, isCurrentBoard, memberList, boardList
        );

        return detailDirectoryResponsetDto;
    }


    @Transactional
    public void leaveDirectory(UserPrincipal userPrincipal, int directoryId) {
        //1. 유저 및 디렉토리 정보 확인
        int userId = userPrincipal.getId();
        Directory directory = directoryRepository.findByDirectoryId(directoryId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY)
        );

        //2. 디렉토리 권한 확인
        UserDirectory userDirectory = userDirectoryRepository
                .findByUser_UserIdAndDirectory_DirectoryIdAndIsAccept(userId, directoryId,true).orElseThrow(
                        ()->new CustomException(ErrorCode.DENIED_DIRECTORY));

        //3. 디렉토리 나가기
        pieceRepository.nullifyUserInPiecesByDirectoryAndUser(userId, directoryId);
        userDirectoryRepository.delete(userDirectory);
        directory.changePeopleNumber(-1);

        //4. 디렉토리 삭제
        if(!userDirectoryRepository.existsByDirectoryAndIsAccept(directory, true)
            || directory.getPeopleNumber() == 0){
            deleteDirectoryData(directoryId, directory);
        }
    }

    @Transactional
    protected void deleteDirectoryData(int directoryId, Directory directory) {
        // 알림 삭제
        List<Notification> notifications = notificationRepository.findByDirectory_DirectoryId(directoryId);
        userNotificationRepository.deleteByNotification(notifications);
        notificationRepository.deleteByNotification(notifications);

        // 퍼즐판 및 퍼즐 조각 삭제
        List<Board> boards = boardRepository.findBoards(directoryId);
        pieceRepository.deletePieceByBoards(boards);
        boardRepository.deleteBoardByBoards(boards);

        // 디렉토리 삭제
        directoryRepository.delete(directory);
    }
}

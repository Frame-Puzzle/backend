package com.frazzle.main.domain.socket.chat.service;

import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.socket.chat.dto.SendMessageDto;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.socket.roby.dto.EntryRobyRequestDto;
import com.frazzle.main.domain.socket.roby.entity.RobyUser;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final UserDirectoryRepository userDirectoryRepository;
    private final UserRepository userRepository;
    private final DirectoryRepository directoryRepository;

    @Transactional
    public EntryRobyRequestDto entryChat(RobyUser robyUser, int boardId, EntryRobyRequestDto sendMessageDto) {

        Directory directory = directoryRepository.findByBoardId(boardId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY)
        );

        User user = userRepository.findByUserId(robyUser.getUserId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        if(!userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user, true)){
            throw new CustomException(ErrorCode.DENIED_PLAY_CHAT);
        }
        String nickname = user.getNickname();
        sendMessageDto.changeNickname(nickname);
        sendMessageDto.changeUserId(user.getUserId());
        sendMessageDto.entryMessage(user.getNickname()+"님이 입장하였습니다.");
        return sendMessageDto;
    }

    @Transactional
    public SendMessageDto exitChat(RobyUser robyUser, int boardId, SendMessageDto sendMessageDto) {

        Directory directory = directoryRepository.findByBoardId(boardId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_DIRECTORY)
        );

        User user = userRepository.findByUserId(robyUser.getUserId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_USER)
        );

        if(!userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user, true)){
            throw new CustomException(ErrorCode.DENIED_PLAY_CHAT);
        }

        String nickname = user.getNickname();
        sendMessageDto.changeNickname(nickname);
        sendMessageDto.changeUserId(user.getUserId());
        sendMessageDto.entryMessage(user.getNickname()+"님이 퇴장하였습니다.");
        return sendMessageDto;
    }
}

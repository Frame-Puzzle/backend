package com.frazzle.main.domain.chat.service;

import com.frazzle.main.domain.chat.dto.SendMessageDto;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import com.frazzle.main.global.models.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final UserDirectoryRepository userDirectoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public SendMessageDto entryChat(UserPrincipal userPrincipal, int directoryId, SendMessageDto sendMessageDto) {
        int userId = userPrincipal.getId();
        if(!userDirectoryRepository.existsByUser_UserIdAndDirectory_DirectoryIdAndIsAccept(userId, directoryId, true)){
            throw new CustomException(ErrorCode.DENIED_PLAY_CHAT);
        }
        String nickname = userRepository.findNicknameByUserId(userId);
        sendMessageDto.changeNickname(nickname);
        sendMessageDto.entryMessage(nickname+"님이 입장하셨습니다.");
        return sendMessageDto;
    }
}

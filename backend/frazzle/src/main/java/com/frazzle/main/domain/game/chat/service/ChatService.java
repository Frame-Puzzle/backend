package com.frazzle.main.domain.game.chat.service;

import com.frazzle.main.domain.game.chat.dto.SendMessageDto;
import com.frazzle.main.domain.directory.repository.DirectoryRepository;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.userdirectory.repository.UserDirectoryRepository;
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
    private final DirectoryRepository directoryRepository;

    @Transactional
    public SendMessageDto entryChat(UserPrincipal userPrincipal, int directoryId, SendMessageDto sendMessageDto) {
//        User user = userPrincipal.getUser();
//        Directory directory = directoryRepository.findById(directoryId).orElseThrow(
//                ()->new CustomException(ErrorCode.NOT_EXIST_DIRECTORY)
//        );
//
//        if(!userDirectoryRepository.existsByDirectoryAndUserAndIsAccept(directory, user, true)){
//            throw new CustomException(ErrorCode.DENIED_PLAY_CHAT);
//        }
//        String nickname = userRepository.findNicknameByUserId(user.getUserId());
        sendMessageDto.changeNickname("nickname");
        sendMessageDto.entryMessage("nickname"+"님이 입장하셨습니다.");
        return sendMessageDto;
    }
}

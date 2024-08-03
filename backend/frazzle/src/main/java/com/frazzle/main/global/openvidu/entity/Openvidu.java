package com.frazzle.main.global.openvidu.entity;

import com.frazzle.main.global.openvidu.dto.OpenviduRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "voice_chats")
public class Openvidu {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voice_chat_id")
    private int voiceChatId;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "board_id")
    private int boardId;

    @Builder
    private Openvidu(String sessionId, int boardId) {
        this.sessionId = sessionId;
        this.boardId = boardId;
    }

    public static Openvidu createOpenvidu(OpenviduRequestDto requestDto) {
        return Openvidu.builder()
                .boardId(requestDto.getBoardId())
                .build();
    }

    public void updateSession(String sessionId) {
        this.sessionId = sessionId;
    }
}

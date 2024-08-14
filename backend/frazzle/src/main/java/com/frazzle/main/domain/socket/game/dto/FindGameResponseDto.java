package com.frazzle.main.domain.socket.game.dto;

import com.frazzle.main.domain.socket.game.entity.GamePlayer;
import com.frazzle.main.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class FindGameResponseDto {
    private List<GamePlayer> userList;

    @Builder
    public FindGameResponseDto(List<GamePlayer> userList) {
        this.userList = userList;
    }

    public static FindGameResponseDto createResponseDto(List<GamePlayer> userList) {
        return FindGameResponseDto.builder()
                .userList(userList)
                .build();
    }
}


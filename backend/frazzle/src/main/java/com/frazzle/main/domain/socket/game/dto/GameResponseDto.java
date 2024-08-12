package com.frazzle.main.domain.socket.game.dto;

import com.frazzle.main.domain.socket.game.entity.Game;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameResponseDto {
    private Game game;

    @Builder
    private GameResponseDto(Game game) {
        this.game = game;
    }

    public static GameResponseDto createResponseDto(Game game) {
        return GameResponseDto.builder()
                .game(game).build();
    }
}

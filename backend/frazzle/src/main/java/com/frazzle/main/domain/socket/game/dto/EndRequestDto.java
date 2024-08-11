package com.frazzle.main.domain.socket.game.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EndRequestDto {
    private Boolean end;
}

package com.frazzle.main.domain.directory.dto;

import com.frazzle.main.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberListDto {

    int userId;
    String nickname;
    String profileUrl;

    @Builder
    private MemberListDto(int userId, String nickname, String profileUrl) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
    }

    public static MemberListDto createMemberList(User user) {
        return MemberListDto.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileImg())
                .build();
    }
}

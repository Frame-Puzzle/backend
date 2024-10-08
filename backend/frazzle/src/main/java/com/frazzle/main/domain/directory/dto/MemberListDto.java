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
    String email;
    boolean isAccept;

    @Builder
    private MemberListDto(int userId, String nickname, String profileUrl, String email, boolean isAccept) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.email = email;
        this.isAccept = isAccept;
    }

    public static MemberListDto createMemberList(User user, boolean isAccept) {
        return MemberListDto.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .profileUrl(user.getProfileImg())
                .email(user.getEmail())
                .isAccept(isAccept)
                .build();
    }
}

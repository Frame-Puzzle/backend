package com.frazzle.main.domain.socket.roby.handler;

import com.frazzle.main.domain.auth.service.JwtTokenService;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.user.service.UserService;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

/*
웹 소켓 시 token 검증
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenService jwtTokenService;
    private final UserService userService;
    private final UserRepository userRepository;
    private String email;

    public String getEmail() {
        return email;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        //토큰 존재 여부 확인
        if (accessor.getCommand() == StompCommand.CONNECT) {
            if (!this.validateAccessToken(accessor.getFirstNativeHeader("Authorization"))) {
                throw new CustomException(ErrorCode.NOT_EXIST_USER);
            }
        }

        return message;
    }

    private boolean validateAccessToken(String accessToken) {
        //토큰 존재 x면 false
        if (accessToken == null) {
            return false;
        }

        String bearerToken = accessToken.trim();

        if (!bearerToken.trim().isEmpty() && bearerToken.startsWith("Bearer ")) {
            accessToken = bearerToken.substring(7);

            try {
                //토큰으로부터 userId를 가져옴
                String payload = jwtTokenService.getPayload(accessToken);
                //유저 정보 찾기
                User user = userRepository.findById(Integer.parseInt(payload)).get();
                //유저의 이메일을 임시 저장
                this.email = user.getEmail();

                return jwtTokenService.validateToken(accessToken);
            } catch (ExpiredJwtException | MalformedJwtException e) {
                return false;
            }
        }

        return false;
    }

    //검증을 할때마다 검증 하는 메서드
    @EventListener(SessionConnectEvent.class)
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String accessToken = accessor.getFirstNativeHeader("Authorization");

        if (this.validateAccessToken(accessToken)) {

            log.info("validate access token"+accessToken);

            //헤더에 senderEmail에 email값을 저장
            accessor.getSessionAttributes().put("senderEmail", email);
        }
    }
}
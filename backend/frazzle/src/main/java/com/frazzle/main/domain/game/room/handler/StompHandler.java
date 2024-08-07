package com.frazzle.main.domain.game.room.handler;

import com.frazzle.main.domain.auth.service.JwtTokenService;
import com.frazzle.main.domain.user.entity.User;
import com.frazzle.main.domain.user.repository.UserRepository;
import com.frazzle.main.domain.user.service.UserService;
import com.frazzle.main.global.models.UserPrincipal;
import com.frazzle.main.global.exception.CustomException;
import com.frazzle.main.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.security.Principal;

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

        if (accessor.getCommand() == StompCommand.CONNECT) {
            if (!this.validateAccessToken(accessor.getFirstNativeHeader("Authorization"))) {
                throw new CustomException(ErrorCode.NOT_EXIST_USER);
            }
        }

        return message;
    }

    private boolean validateAccessToken(String accessToken) {
        if (accessToken == null) {
            return false;
        }

        String bearerToken = accessToken.trim();

        if (!bearerToken.trim().isEmpty() && bearerToken.startsWith("Bearer ")) {
            accessToken = bearerToken.substring(7);

            try {
                String payload = jwtTokenService.getPayload(accessToken);
                User user = userRepository.findById(Integer.parseInt(payload)).get();
                this.email = user.getEmail();

                return jwtTokenService.validateToken(accessToken);
            } catch (ExpiredJwtException | MalformedJwtException e) {
                return false;
            }
        }

        return false;
    }

    @EventListener(SessionConnectEvent.class)
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String accessToken = accessor.getFirstNativeHeader("Authorization");

        if (this.validateAccessToken(accessToken)) {
            log.info("validate access token"+accessToken);
            accessor.getSessionAttributes().put("senderEmail", email);
        }
    }
}
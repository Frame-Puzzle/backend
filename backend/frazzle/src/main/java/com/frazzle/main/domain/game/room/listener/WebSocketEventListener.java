//package com.frazzle.main.domain.game.room.listener;
//
//import com.frazzle.main.domain.game.room.service.RoomService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.event.EventListener;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.messaging.SessionConnectedEvent;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class WebSocketEventListener {
//
//    private final SimpMessagingTemplate messagingTemplate;
//    private final RoomService roomService;
//
//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        String username = (String) headerAccessor.getSessionAttributes().get("nickname");
//        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
////        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
////        String roomId = "1";
//        if (roomId != null) {
//            roomService.addUserToRoom(Integer.parseInt(roomId), username);
//            log.info("User Connected: " + username + " to room " + roomId);
//            broadcastRoomUsers(1);
//        }
//    }
//
//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        String username = headerAccessor.getUser().getName();
//        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
//        if (roomId != null) {
//
//            roomService.removeUserFromRoom(Integer.parseInt(roomId), username);
//            log.info("User Disconnected: " + username + " from room " + roomId);
//            broadcastRoomUsers(1);
//        }
//    }
//
//    private void broadcastRoomUsers(int roomId) {
//        messagingTemplate.convertAndSend("/sub/room/" + 1 + "/users", roomService.getUsersInRoom(roomId));
//    }
//}
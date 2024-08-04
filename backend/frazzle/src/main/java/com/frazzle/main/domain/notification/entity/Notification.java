package com.frazzle.main.domain.notification.entity;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "notifications")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private int notificationId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "keyword", nullable = false, length = 10)
    private String keyword;

    @Column(name = "type", nullable = false)
    private String type;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directory_id")
    private Directory directory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    private Notification(String keyword, String type, User user, Directory directory, Board board) {
        this.keyword = keyword;
        this.type = type;
        this.user = user;
        this.directory = directory;
        this.board = board;
    }

    public static Notification createNotificationWithDirectory(String keyword, String type, User user, Directory directory) {
        return Notification.builder()
                .keyword(keyword)
                .type(type)
                .user(user)
                .directory(directory)
                .build();
    }

    public static Notification createNotificationWithBoard(String keyword, String type, User user, Directory directory, Board board) {
        return Notification.builder()
                .keyword(keyword)
                .type(type)
                .user(user)
                .directory(directory)
                .board(board)
                .build();
    }

}

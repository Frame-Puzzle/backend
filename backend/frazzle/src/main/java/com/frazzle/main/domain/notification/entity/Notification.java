package com.frazzle.main.domain.notification.entity;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.directory.entity.Directory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "push_notifications")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "push_id")
    private int push_id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "keyword", nullable = false, length = 10)
    private String keyword;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "create_user", nullable = false, length = 32)
    private String createUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directory_id")
    private Directory directory;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "board_id")
//    private Board board;


    @Builder
    public Notification(String keyword, int type, String createUser, Directory directory) {
        this.keyword = keyword;
        this.type = type;
        this.createUser = createUser;
        this.directory = directory;
//        this.board = null;
    }

//    @Builder Notification(String keyword, int type, String createUser , Board board) {
//        this.keyword = keyword;
//        this.type = type;
//        this.createUser = createUser;
//        this.directory = null;
//        this.board = board;
//    }

    public static Notification createNotificationWithDirectory(String keyword, int type, String createUser, Directory directory) {
        return Notification.builder()
                .keyword(keyword)
                .type(type)
                .createUser(createUser)
                .directory(directory)
                .build();
    }

//    public static Notification createNotificationWithBoard(String keyword, int type, String createUser, Board board) {
//        return Notification.builder()
//                .keyword(keyword)
//                .type(type)
//                .createUser(createUser)
//                .board(board);
//    }

}

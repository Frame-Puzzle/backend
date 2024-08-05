package com.frazzle.main.domain.piece.entity;

import com.frazzle.main.domain.board.entity.Board;
import com.frazzle.main.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "pieces")
public class Piece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "piece_id")
    private int pieceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    //퍼즐조각을 수정한 유저 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @UpdateTimestamp
    @Column(name = "modified_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime modifiedAt;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "mission_name", length = 128)
    private String missionName;

    @Column()
    private String content;

    //사람 수
    @Column(name = "people_count")
    private int peopleCount;

    @Column(name = "piece_row", nullable = false)
    private int pieceRow;

    @Column(name = "piece_col", nullable = false)
    private int pieceCol;

    @Builder
    private Piece(Board board, int pieceRow, int pieceCol)
    {
        this.board = board;
        this.pieceRow = pieceRow;
        this.pieceCol = pieceCol;
    }

    public static Piece createPiece(Board board, int pieceRow, int pieceCol) {
        return Piece.builder()
                .board(board)
                .pieceRow(pieceRow)
                .pieceCol(pieceCol)
                .build();
    }

    public void updatePieceDto(String imageUrl, String content, User user, int peopleCount) {
        //사용자 유저 정보가 없을때, 혹은 같을 때 수정 가능(서비스 쪽에서 처리)
        this.imageUrl = imageUrl;
        this.content = content;
        this.user = user;
        this.peopleCount = peopleCount;
        this.modifiedAt = LocalDateTime.now();
    }

    public void updateMission(String missionName){
        this.missionName = missionName;
    }

    public void updateUser(User user){
        this.user = user;
    }
}

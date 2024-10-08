package com.frazzle.main.domain.board.entity;

import com.frazzle.main.domain.board.dto.CreateBoardRequestDto;
import com.frazzle.main.domain.directory.entity.Directory;
import com.frazzle.main.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private int boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directory_id")
    private Directory directory;

    @Column(name = "board_in_number")
    private int boardInNumber;

    //최대 3가지 키워드를 '#'으로 구분해서 담아둔다.
    @Column(name = "keyword", length = 64)
    private String keyword;

    @Column(name = "board_size", nullable = false)
    private int boardSize;

    /**
     * clear_type
     * 보드판의 상태를 표현한다.
     * 0: 아무 상태 아님
     * 1: 퍼즐 완성
     * 2: 퍼즐 게임까지 완료
     */
    @Column(name = "clear_type", nullable = false)
    private int clearType;

    @Column(name = "piece_count", nullable = false)
    private int pieceCount;

    //퍼즐 게임 클리어 시 삽입될 이미지
    @Column(name = "thumbnail_url", nullable = true, length = 255)
    private String thumbnailUrl;

    @Column(name = "is_vote", nullable = false)
    private boolean isVote;

    @Column(name = "vote_number", nullable = false)
    private int voteNumber;

    //clear_type와 중복 사용이라 판단
    //@Column(name = "is_complete", nullable = false)
    //private boolean isComplete;

    @Builder
    private Board(User user,
                  Directory directory,
                  int boardInNumber,
                  String keyword,
                  int boardSize,
                  int clearType,
                  int pieceCount,
                  String thumbnailUrl,
                  boolean isVote,
                  int voteNumber
                  //boolean isComplete
                    )
    {
        this.user = user;
        this.directory = directory;
        this.boardInNumber = boardInNumber;
        this.keyword = keyword;
        this.boardSize = boardSize;
        this.clearType = clearType;
        this.pieceCount = pieceCount;
        this.thumbnailUrl = thumbnailUrl;
        this.isVote = isVote;
        this.voteNumber = voteNumber;
        //this.isComplete = isComplete;
    }

    public static Board createBoard(CreateBoardRequestDto boardDto, Directory directory, String keyword){
        return Board.builder()
                //.user(user)
                .directory(directory)
                .boardInNumber(directory.getBoardNumber())
                .keyword(keyword)
                .boardSize(boardDto.getBoardSize())
                .clearType(0)
                .pieceCount(0)
                .thumbnailUrl(null)
                .isVote(false)
                .voteNumber(0)
                .build();
    }

    public void updateUser(User user){this.user = user;}
    //public void updateDirectory(Directory directory){this.directory = directory;}

    public void changeClearType(BoardClearTypeFlag type){
        this.clearType = type.getValue();
    }
    public void changeImageUrl(String imageUrl){
        this.thumbnailUrl = imageUrl;
    }
    public void addPieceCount(){
        this.pieceCount++;
    }
    public void changePieceCount(int number)
    {
        this.pieceCount = number;
    }

    //toggle
    public void changeVote(){
        this.isVote = !this.isVote;
    }

    public void enableVote(boolean enable){
        this.isVote = enable;
    }

    public void changeVoteNumber(int number){
        this.voteNumber = number;
    }
    public void addVoteNumber(){
        this.voteNumber++;
    }
    public void changeBoardInNumber(int number){ this.boardInNumber = number; }

    public Boolean getVote() {
        return this.isVote;
    }
}

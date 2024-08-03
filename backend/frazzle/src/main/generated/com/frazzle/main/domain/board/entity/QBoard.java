package com.frazzle.main.domain.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = -677781467L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final NumberPath<Integer> boardId = createNumber("boardId", Integer.class);

    public final NumberPath<Integer> boardInNumber = createNumber("boardInNumber", Integer.class);

    public final NumberPath<Integer> boardSize = createNumber("boardSize", Integer.class);

    public final NumberPath<Integer> clearType = createNumber("clearType", Integer.class);

    public final com.frazzle.main.domain.directory.entity.QDirectory directory;

    public final BooleanPath isVote = createBoolean("isVote");

    public final StringPath keyword = createString("keyword");

    public final NumberPath<Integer> pieceCount = createNumber("pieceCount", Integer.class);

    public final StringPath thumbnailUrl = createString("thumbnailUrl");

    public final com.frazzle.main.domain.user.entity.QUser user;

    public final NumberPath<Integer> voteNumber = createNumber("voteNumber", Integer.class);

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.directory = inits.isInitialized("directory") ? new com.frazzle.main.domain.directory.entity.QDirectory(forProperty("directory")) : null;
        this.user = inits.isInitialized("user") ? new com.frazzle.main.domain.user.entity.QUser(forProperty("user")) : null;
    }

}


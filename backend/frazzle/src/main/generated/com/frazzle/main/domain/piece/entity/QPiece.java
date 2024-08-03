package com.frazzle.main.domain.piece.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPiece is a Querydsl query type for Piece
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPiece extends EntityPathBase<Piece> {

    private static final long serialVersionUID = -660230875L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPiece piece = new QPiece("piece");

    public final com.frazzle.main.domain.board.entity.QBoard board;

    public final StringPath content = createString("content");

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath missionName = createString("missionName");

    public final DateTimePath<java.time.LocalDateTime> modifiedAt = createDateTime("modifiedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> peopleCount = createNumber("peopleCount", Integer.class);

    public final NumberPath<Integer> pieceCol = createNumber("pieceCol", Integer.class);

    public final NumberPath<Integer> pieceId = createNumber("pieceId", Integer.class);

    public final NumberPath<Integer> pieceRow = createNumber("pieceRow", Integer.class);

    public final com.frazzle.main.domain.user.entity.QUser user;

    public QPiece(String variable) {
        this(Piece.class, forVariable(variable), INITS);
    }

    public QPiece(Path<? extends Piece> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPiece(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPiece(PathMetadata metadata, PathInits inits) {
        this(Piece.class, metadata, inits);
    }

    public QPiece(Class<? extends Piece> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.frazzle.main.domain.board.entity.QBoard(forProperty("board"), inits.get("board")) : null;
        this.user = inits.isInitialized("user") ? new com.frazzle.main.domain.user.entity.QUser(forProperty("user")) : null;
    }

}


package com.frazzle.main.global.openvidu.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOpenvidu is a Querydsl query type for Openvidu
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOpenvidu extends EntityPathBase<Openvidu> {

    private static final long serialVersionUID = -1241859812L;

    public static final QOpenvidu openvidu = new QOpenvidu("openvidu");

    public final NumberPath<Integer> boardId = createNumber("boardId", Integer.class);

    public final StringPath sessionId = createString("sessionId");

    public final NumberPath<Integer> voiceChatId = createNumber("voiceChatId", Integer.class);

    public QOpenvidu(String variable) {
        super(Openvidu.class, forVariable(variable));
    }

    public QOpenvidu(Path<? extends Openvidu> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOpenvidu(PathMetadata metadata) {
        super(Openvidu.class, metadata);
    }

}


package com.frazzle.main.domain.directory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDirectory is a Querydsl query type for Directory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDirectory extends EntityPathBase<Directory> {

    private static final long serialVersionUID = 1579181701L;

    public static final QDirectory directory = new QDirectory("directory");

    public final NumberPath<Integer> boardNumber = createNumber("boardNumber", Integer.class);

    public final StringPath category = createString("category");

    public final NumberPath<Integer> directoryId = createNumber("directoryId", Integer.class);

    public final StringPath directoryName = createString("directoryName");

    public final DateTimePath<java.time.LocalDateTime> modifiedAt = createDateTime("modifiedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> peopleNumber = createNumber("peopleNumber", Integer.class);

    public QDirectory(String variable) {
        super(Directory.class, forVariable(variable));
    }

    public QDirectory(Path<? extends Directory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDirectory(PathMetadata metadata) {
        super(Directory.class, metadata);
    }

}


package com.salmalteam.salmal.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseCreatedTimeEntity is a Querydsl query type for BaseCreatedTimeEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseCreatedTimeEntity extends EntityPathBase<BaseCreatedTimeEntity> {

    private static final long serialVersionUID = 1782124007L;

    public static final QBaseCreatedTimeEntity baseCreatedTimeEntity = new QBaseCreatedTimeEntity("baseCreatedTimeEntity");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public QBaseCreatedTimeEntity(String variable) {
        super(BaseCreatedTimeEntity.class, forVariable(variable));
    }

    public QBaseCreatedTimeEntity(Path<? extends BaseCreatedTimeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseCreatedTimeEntity(PathMetadata metadata) {
        super(BaseCreatedTimeEntity.class, metadata);
    }

}


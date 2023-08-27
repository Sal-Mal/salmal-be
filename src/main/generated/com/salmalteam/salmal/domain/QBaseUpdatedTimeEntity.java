package com.salmalteam.salmal.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseUpdatedTimeEntity is a Querydsl query type for BaseUpdatedTimeEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseUpdatedTimeEntity extends EntityPathBase<BaseUpdatedTimeEntity> {

    private static final long serialVersionUID = -114588678L;

    public static final QBaseUpdatedTimeEntity baseUpdatedTimeEntity = new QBaseUpdatedTimeEntity("baseUpdatedTimeEntity");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QBaseUpdatedTimeEntity(String variable) {
        super(BaseUpdatedTimeEntity.class, forVariable(variable));
    }

    public QBaseUpdatedTimeEntity(Path<? extends BaseUpdatedTimeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseUpdatedTimeEntity(PathMetadata metadata) {
        super(BaseUpdatedTimeEntity.class, metadata);
    }

}


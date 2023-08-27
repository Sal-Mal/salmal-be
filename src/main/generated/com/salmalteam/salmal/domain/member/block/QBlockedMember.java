package com.salmalteam.salmal.domain.member.block;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBlockedMember is a Querydsl query type for BlockedMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlockedMember extends EntityPathBase<BlockedMember> {

    private static final long serialVersionUID = 462497745L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBlockedMember blockedMember1 = new QBlockedMember("blockedMember1");

    public final com.salmalteam.salmal.domain.QBaseCreatedTimeEntity _super = new com.salmalteam.salmal.domain.QBaseCreatedTimeEntity(this);

    public final com.salmalteam.salmal.domain.member.QMember blockedMember;

    public final com.salmalteam.salmal.domain.member.QMember blocker;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QBlockedMember(String variable) {
        this(BlockedMember.class, forVariable(variable), INITS);
    }

    public QBlockedMember(Path<? extends BlockedMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBlockedMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBlockedMember(PathMetadata metadata, PathInits inits) {
        this(BlockedMember.class, metadata, inits);
    }

    public QBlockedMember(Class<? extends BlockedMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.blockedMember = inits.isInitialized("blockedMember") ? new com.salmalteam.salmal.domain.member.QMember(forProperty("blockedMember"), inits.get("blockedMember")) : null;
        this.blocker = inits.isInitialized("blocker") ? new com.salmalteam.salmal.domain.member.QMember(forProperty("blocker"), inits.get("blocker")) : null;
    }

}


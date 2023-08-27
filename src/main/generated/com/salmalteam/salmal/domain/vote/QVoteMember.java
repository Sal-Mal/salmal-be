package com.salmalteam.salmal.domain.vote;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVoteMember is a Querydsl query type for VoteMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVoteMember extends EntityPathBase<VoteMember> {

    private static final long serialVersionUID = 1142443656L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVoteMember voteMember = new QVoteMember("voteMember");

    public final com.salmalteam.salmal.domain.QBaseCreatedTimeEntity _super = new com.salmalteam.salmal.domain.QBaseCreatedTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QVote vote;

    public final com.salmalteam.salmal.domain.member.QMember voter;

    public QVoteMember(String variable) {
        this(VoteMember.class, forVariable(variable), INITS);
    }

    public QVoteMember(Path<? extends VoteMember> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVoteMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVoteMember(PathMetadata metadata, PathInits inits) {
        this(VoteMember.class, metadata, inits);
    }

    public QVoteMember(Class<? extends VoteMember> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.vote = inits.isInitialized("vote") ? new QVote(forProperty("vote"), inits.get("vote")) : null;
        this.voter = inits.isInitialized("voter") ? new com.salmalteam.salmal.domain.member.QMember(forProperty("voter"), inits.get("voter")) : null;
    }

}


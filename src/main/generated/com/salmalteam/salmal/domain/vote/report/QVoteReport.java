package com.salmalteam.salmal.domain.vote.report;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVoteReport is a Querydsl query type for VoteReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVoteReport extends EntityPathBase<VoteReport> {

    private static final long serialVersionUID = -1981397740L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVoteReport voteReport = new QVoteReport("voteReport");

    public final com.salmalteam.salmal.domain.QBaseCreatedTimeEntity _super = new com.salmalteam.salmal.domain.QBaseCreatedTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.salmalteam.salmal.domain.member.QMember reporter;

    public final com.salmalteam.salmal.domain.vote.QVote vote;

    public QVoteReport(String variable) {
        this(VoteReport.class, forVariable(variable), INITS);
    }

    public QVoteReport(Path<? extends VoteReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVoteReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVoteReport(PathMetadata metadata, PathInits inits) {
        this(VoteReport.class, metadata, inits);
    }

    public QVoteReport(Class<? extends VoteReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reporter = inits.isInitialized("reporter") ? new com.salmalteam.salmal.domain.member.QMember(forProperty("reporter"), inits.get("reporter")) : null;
        this.vote = inits.isInitialized("vote") ? new com.salmalteam.salmal.domain.vote.QVote(forProperty("vote"), inits.get("vote")) : null;
    }

}


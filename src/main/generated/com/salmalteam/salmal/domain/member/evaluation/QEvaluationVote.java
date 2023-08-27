package com.salmalteam.salmal.domain.member.evaluation;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEvaluationVote is a Querydsl query type for EvaluationVote
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEvaluationVote extends EntityPathBase<EvaluationVote> {

    private static final long serialVersionUID = -929994684L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEvaluationVote evaluationVote = new QEvaluationVote("evaluationVote");

    public final com.salmalteam.salmal.domain.QBaseCreatedTimeEntity _super = new com.salmalteam.salmal.domain.QBaseCreatedTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.salmalteam.salmal.domain.member.QMember evaluator;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.salmalteam.salmal.domain.vote.QVote vote;

    public QEvaluationVote(String variable) {
        this(EvaluationVote.class, forVariable(variable), INITS);
    }

    public QEvaluationVote(Path<? extends EvaluationVote> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEvaluationVote(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEvaluationVote(PathMetadata metadata, PathInits inits) {
        this(EvaluationVote.class, metadata, inits);
    }

    public QEvaluationVote(Class<? extends EvaluationVote> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.evaluator = inits.isInitialized("evaluator") ? new com.salmalteam.salmal.domain.member.QMember(forProperty("evaluator"), inits.get("evaluator")) : null;
        this.vote = inits.isInitialized("vote") ? new com.salmalteam.salmal.domain.vote.QVote(forProperty("vote"), inits.get("vote")) : null;
    }

}


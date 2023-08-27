package com.salmalteam.salmal.domain.review.report;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewReport is a Querydsl query type for ReviewReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewReport extends EntityPathBase<ReviewReport> {

    private static final long serialVersionUID = -1873251600L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewReport reviewReport = new QReviewReport("reviewReport");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.salmalteam.salmal.domain.member.QMember reporter;

    public final com.salmalteam.salmal.domain.review.QReview review;

    public QReviewReport(String variable) {
        this(ReviewReport.class, forVariable(variable), INITS);
    }

    public QReviewReport(Path<? extends ReviewReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewReport(PathMetadata metadata, PathInits inits) {
        this(ReviewReport.class, metadata, inits);
    }

    public QReviewReport(Class<? extends ReviewReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reporter = inits.isInitialized("reporter") ? new com.salmalteam.salmal.domain.member.QMember(forProperty("reporter"), inits.get("reporter")) : null;
        this.review = inits.isInitialized("review") ? new com.salmalteam.salmal.domain.review.QReview(forProperty("review"), inits.get("review")) : null;
    }

}


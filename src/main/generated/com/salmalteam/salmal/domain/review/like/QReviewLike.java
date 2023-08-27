package com.salmalteam.salmal.domain.review.like;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewLike is a Querydsl query type for ReviewLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewLike extends EntityPathBase<ReviewLike> {

    private static final long serialVersionUID = -849778544L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewLike reviewLike = new QReviewLike("reviewLike");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.salmalteam.salmal.domain.member.QMember liker;

    public final com.salmalteam.salmal.domain.review.QReview review;

    public QReviewLike(String variable) {
        this(ReviewLike.class, forVariable(variable), INITS);
    }

    public QReviewLike(Path<? extends ReviewLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewLike(PathMetadata metadata, PathInits inits) {
        this(ReviewLike.class, metadata, inits);
    }

    public QReviewLike(Class<? extends ReviewLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.liker = inits.isInitialized("liker") ? new com.salmalteam.salmal.domain.member.QMember(forProperty("liker"), inits.get("liker")) : null;
        this.review = inits.isInitialized("review") ? new com.salmalteam.salmal.domain.review.QReview(forProperty("review"), inits.get("review")) : null;
    }

}


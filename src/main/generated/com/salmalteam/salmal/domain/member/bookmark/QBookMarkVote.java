package com.salmalteam.salmal.domain.member.bookmark;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookMarkVote is a Querydsl query type for BookMarkVote
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookMarkVote extends EntityPathBase<BookMarkVote> {

    private static final long serialVersionUID = 1955641380L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookMarkVote bookMarkVote = new QBookMarkVote("bookMarkVote");

    public final com.salmalteam.salmal.domain.QBaseCreatedTimeEntity _super = new com.salmalteam.salmal.domain.QBaseCreatedTimeEntity(this);

    public final com.salmalteam.salmal.domain.member.QMember bookmaker;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.salmalteam.salmal.domain.vote.QVote vote;

    public QBookMarkVote(String variable) {
        this(BookMarkVote.class, forVariable(variable), INITS);
    }

    public QBookMarkVote(Path<? extends BookMarkVote> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookMarkVote(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookMarkVote(PathMetadata metadata, PathInits inits) {
        this(BookMarkVote.class, metadata, inits);
    }

    public QBookMarkVote(Class<? extends BookMarkVote> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bookmaker = inits.isInitialized("bookmaker") ? new com.salmalteam.salmal.domain.member.QMember(forProperty("bookmaker"), inits.get("bookmaker")) : null;
        this.vote = inits.isInitialized("vote") ? new com.salmalteam.salmal.domain.vote.QVote(forProperty("vote"), inits.get("vote")) : null;
    }

}


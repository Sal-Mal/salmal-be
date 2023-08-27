package com.salmalteam.salmal.domain.vote;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QVoteImage is a Querydsl query type for VoteImage
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QVoteImage extends BeanPath<VoteImage> {

    private static final long serialVersionUID = 587575213L;

    public static final QVoteImage voteImage = new QVoteImage("voteImage");

    public final StringPath imageUrl = createString("imageUrl");

    public QVoteImage(String variable) {
        super(VoteImage.class, forVariable(variable));
    }

    public QVoteImage(Path<? extends VoteImage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVoteImage(PathMetadata metadata) {
        super(VoteImage.class, metadata);
    }

}


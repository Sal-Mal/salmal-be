package com.salmalteam.salmal.domain.review;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QContent is a Querydsl query type for Content
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QContent extends BeanPath<Content> {

    private static final long serialVersionUID = -656451325L;

    public static final QContent content = new QContent("content");

    public final StringPath value = createString("value");

    public QContent(String variable) {
        super(Content.class, forVariable(variable));
    }

    public QContent(Path<? extends Content> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContent(PathMetadata metadata) {
        super(Content.class, metadata);
    }

}


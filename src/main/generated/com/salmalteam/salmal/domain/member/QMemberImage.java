package com.salmalteam.salmal.domain.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberImage is a Querydsl query type for MemberImage
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QMemberImage extends BeanPath<MemberImage> {

    private static final long serialVersionUID = -1110749523L;

    public static final QMemberImage memberImage = new QMemberImage("memberImage");

    public final StringPath imageUrl = createString("imageUrl");

    public QMemberImage(String variable) {
        super(MemberImage.class, forVariable(variable));
    }

    public QMemberImage(Path<? extends MemberImage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberImage(PathMetadata metadata) {
        super(MemberImage.class, metadata);
    }

}


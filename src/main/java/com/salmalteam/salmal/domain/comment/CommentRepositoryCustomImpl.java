package com.salmalteam.salmal.domain.comment;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.salmalteam.salmal.dto.request.comment.CommentPageRequest;
import com.salmalteam.salmal.dto.request.comment.ReplyPageRequest;
import com.salmalteam.salmal.dto.response.comment.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.salmalteam.salmal.domain.comment.QComment.comment;
import static com.salmalteam.salmal.domain.comment.like.QCommentLike.commentLike;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public CommentPageResponse searchList(final Long voteId, final Long memberId, final CommentPageRequest commentPageRequest) {

        final List<CommentResponse> commentResponses = jpaQueryFactory.select(new QCommentResponse(
                        comment.id,
                        comment.commenter.id,
                        comment.commenter.nickName.value,
                        comment.commenter.memberImage.imageUrl,
                        commentLike.isNotNull(),
                        comment.likeCount,
                        comment.replyCount,
                        comment.content.value,
                        comment.createAt,
                        comment.updateAt))
                .from(comment)
                .leftJoin(commentLike)
                .on(commentLike.comment.id.eq(comment.id).and(commentLike.liker.id.eq(memberId)))
                .where(
                        comment.vote.id.eq(voteId),
                        comment.commentType.eq(CommentType.COMMENT),
                        cursorId(commentPageRequest.getCursorId())
                )
                .orderBy(comment.id.desc())
                .limit(commentPageRequest.getSize() + 1)
                .fetch();

        final boolean hasNext = isHasNext(commentResponses, commentPageRequest.getSize());
        return CommentPageResponse.of(hasNext, commentResponses);
    }

    @Override
    public ReplyPageResponse searchReplies(final Long parentCommentId, final Long memberId, final ReplyPageRequest replyPageRequest){
        final List<ReplyResponse> replyResponses = jpaQueryFactory.select(new QReplyResponse(
                        comment.id,
                        comment.commenter.id,
                        comment.commenter.nickName.value,
                        comment.commenter.memberImage.imageUrl,
                        commentLike.isNotNull(),
                        comment.likeCount,
                        comment.content.value,
                        comment.createAt,
                        comment.updateAt))
                .from(comment)
                .leftJoin(commentLike)
                .on(commentLike.comment.id.eq(comment.id).and(commentLike.liker.id.eq(memberId)))
                .where(
                        comment.parentComment.id.eq(parentCommentId),
                        cursorId(replyPageRequest.getCursorId())
                )
                .orderBy(comment.id.asc())
                .limit(replyPageRequest.getSize() + 1)
                .fetch();

        final boolean hasNext = isHasNext(replyResponses, replyPageRequest.getSize());
        return ReplyPageResponse.of(hasNext, replyResponses);
    }

    @Override
    public List<ReplyResponse> searchAllReplies(final Long parentCommentId, final Long memberId){
        return jpaQueryFactory.select(new QReplyResponse(
                        comment.id,
                        comment.commenter.id,
                        comment.commenter.nickName.value,
                        comment.commenter.memberImage.imageUrl,
                        commentLike.isNotNull(),
                        comment.likeCount,
                        comment.content.value,
                        comment.createAt,
                        comment.updateAt))
                .from(comment)
                .leftJoin(commentLike)
                .on(commentLike.comment.id.eq(comment.id).and(commentLike.liker.id.eq(memberId)))
                .where(
                        comment.parentComment.id.eq(parentCommentId)
                )
                .orderBy(comment.id.asc())
                .fetch();
    }

    /**
     * iOS 요청 (임시) 사항 : 댓글 전체 조회 기능
     * 페이지네이션 적용 안함
     */
    @Override
    public List<CommentResponse> searchAllList(final Long voteId, final Long memberId){

        return jpaQueryFactory.select(new QCommentResponse(
                        comment.id,
                        comment.commenter.id,
                        comment.commenter.nickName.value,
                        comment.commenter.memberImage.imageUrl,
                        commentLike.isNotNull(),
                        comment.likeCount,
                        comment.replyCount,
                        comment.content.value,
                        comment.createAt,
                        comment.updateAt))
                .from(comment)
                .leftJoin(commentLike)
                .on(commentLike.comment.id.eq(comment.id).and(commentLike.liker.id.eq(memberId)))
                .where(
                        comment.commentType.eq(CommentType.COMMENT),
                        comment.vote.id.eq(voteId)
                )
                .orderBy(comment.id.desc())
                .fetch();

    }



    private boolean isHasNext(List<?> result, final int pageSize) {
        boolean hasNext = false;
        if (result.size() > pageSize) {
            hasNext = true;
            result.remove(pageSize);
        }
        return hasNext;
    }

    private BooleanExpression cursorId(final Long cursorId) {
        return cursorId == null ? null : comment.id.lt(cursorId);
    }
}

package com.salmalteam.salmal.domain.vote;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.salmalteam.salmal.dto.request.member.vote.MemberBookmarkVotePageRequest;
import com.salmalteam.salmal.dto.request.member.vote.MemberEvaluationVotePageRequest;
import com.salmalteam.salmal.dto.request.member.vote.MemberVotePageRequest;
import com.salmalteam.salmal.dto.request.vote.VotePageRequest;
import com.salmalteam.salmal.dto.response.member.vote.*;
import com.salmalteam.salmal.dto.response.vote.QVoteResponse;
import com.salmalteam.salmal.dto.response.vote.VotePageResponse;
import com.salmalteam.salmal.dto.response.vote.VoteResponse;
import com.salmalteam.salmal.presentation.vote.SearchTypeConstant;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.salmalteam.salmal.domain.vote.QVote.vote;
import static com.salmalteam.salmal.domain.vote.bookmark.QVoteBookMark.voteBookMark;
import static com.salmalteam.salmal.domain.vote.evaluation.QVoteEvaluation.voteEvaluation;

@RequiredArgsConstructor
public class VoteRepositoryCustomImpl implements VoteRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public VoteResponse search(final Long id, final Long memberId) {

        final BooleanExpression bookmarkSubQuery = createBookmarkSubQuery(id, memberId);
        final JPQLQuery<String> voteEvaluationSubQuery = createVoteEvaluationSubQuery(id, memberId);

        return queryFactory.select(new QVoteResponse(
                        vote.id,
                        vote.member.id,
                        vote.voteImage.imageUrl,
                        vote.member.nickName.value,
                        vote.member.memberImage.imageUrl,
                        vote.commentCount,
                        vote.likeCount,
                        vote.dislikeCount,
                        vote.evaluationCount,
                        vote.likeRatio,
                        vote.dislikeRatio,
                        vote.createdAt,
                        bookmarkSubQuery,
                        voteEvaluationSubQuery))
                .from(vote)
                .where(vote.id.eq(id))
                .fetchOne();
    }

    private BooleanExpression createBookmarkSubQuery(final Long id, final Long memberId) {
        return JPAExpressions.selectFrom(voteBookMark)
                .where(
                        voteBookMark.vote.id.eq(id),
                        voteBookMark.bookmaker.id.eq(memberId)
                )
                .exists();
    }

    private JPQLQuery<String> createVoteEvaluationSubQuery(final Long id, final Long memberId) {
        return JPAExpressions.select(
                        voteEvaluation.voteEvaluationType.stringValue()
                )
                .where(
                        voteEvaluation.vote.id.eq(id),
                        voteEvaluation.evaluator.id.eq(memberId)
                )
                .from(voteEvaluation);
    }

    /**
     * TODO
     * BEST : 내가 차단한 사람 조회 필터링
     * HOME : 랜덤 조회
     */
    @Override
    public VotePageResponse searchList(final Long memberId, final VotePageRequest votePageRequest, final SearchTypeConstant searchTypeConstant) {

        // BEST : 좋아요 기준 내림차순 조회
        // HOME : 랜덤 조회
        final List<VoteResponse> voteResponses = queryFactory.select(new QVoteResponse(
                        vote.id,
                        vote.member.id,
                        vote.voteImage.imageUrl,
                        vote.member.nickName.value,
                        vote.member.memberImage.imageUrl,
                        vote.commentCount,
                        vote.likeCount,
                        vote.dislikeCount,
                        vote.evaluationCount,
                        vote.likeRatio,
                        vote.dislikeRatio,
                        vote.createdAt,
                        voteBookMark.isNotNull(),
                        voteEvaluation.voteEvaluationType.stringValue()))
                .from(vote)
                .leftJoin(voteEvaluation)
                .on(vote.id.eq(voteEvaluation.vote.id).and(voteEvaluation.evaluator.id.eq(memberId)))
                .leftJoin(voteBookMark)
                .on(vote.id.eq(voteBookMark.vote.id).and(voteBookMark.bookmaker.id.eq(memberId)))
                .where(
                        cursorLikeCountAndCursorId(votePageRequest.getCursorId(), votePageRequest.getCursorLikes(), searchTypeConstant)
                )
                .orderBy(orderSpecifiers(searchTypeConstant))
                .limit(votePageRequest.getSize() + 1)
                .fetch();

        final boolean hasNext = isHasNext(voteResponses, votePageRequest.getSize());
        return VotePageResponse.of(hasNext, voteResponses);
    }

    private BooleanExpression cursorLikeCountAndCursorId(final Long cursorId, final Integer cursorLikeCount, final SearchTypeConstant searchTypeConstant) {

        if (cursorId != null && searchTypeConstant.equals(SearchTypeConstant.HOME)) return vote.id.lt(cursorId);
        if (cursorLikeCount == null || cursorId == null) return null;

        return vote.likeCount.eq(cursorLikeCount)
                .and(vote.id.lt(cursorId))
                .or(vote.likeCount.lt(cursorLikeCount));
    }

    @Override
    public MemberVotePageResponse searchMemberVoteList(final Long memberId, final MemberVotePageRequest memberVotePageRequest) {

        final List<MemberVoteResponse> memberVoteResponses = queryFactory.select(new QMemberVoteResponse(
                        vote.id,
                        vote.voteImage.imageUrl,
                        vote.createdAt
                ))
                .from(vote)
                .where(
                        vote.member.id.eq(memberId)
                )
                .orderBy(
                        vote.id.desc()
                )
                .limit(memberVotePageRequest.getSize() + 1)
                .fetch();

        final boolean hasNext = isHasNext(memberVoteResponses, memberVotePageRequest.getSize());
        return MemberVotePageResponse.of(hasNext, memberVoteResponses);
    }

    @Override
    public MemberEvaluationVotePageResponse searchMemberEvaluationVoteList(final Long memberId, final MemberEvaluationVotePageRequest memberEvaluationVotePageRequest) {

        final List<MemberEvaluationVoteResponse> memberEvaluationVoteResponses = queryFactory.select(new QMemberEvaluationVoteResponse(
                        vote.id,
                        vote.voteImage.imageUrl,
                        voteEvaluation.createdAt
                ))
                .from(vote)
                .innerJoin(voteEvaluation)
                .on(vote.id.eq(voteEvaluation.vote.id))
                .where(
                        voteEvaluation.evaluator.id.eq(memberId),
                        ltId(memberEvaluationVotePageRequest.getCursorId())
                )
                .orderBy(
                        vote.id.desc()
                )
                .limit(memberEvaluationVotePageRequest.getSize() + 1)
                .fetch();

        final boolean hasNext = isHasNext(memberEvaluationVoteResponses, memberEvaluationVotePageRequest.getSize());
        return MemberEvaluationVotePageResponse.of(hasNext, memberEvaluationVoteResponses);
    }

    @Override
    public MemberBookmarkVotePageResponse searchMemberBookmarkVoteList(final Long memberId, final MemberBookmarkVotePageRequest memberBookmarkVotePageRequest) {

        final List<MemberBookmarkVoteResponse> memberBookmarkVoteResponses = queryFactory.select(new QMemberBookmarkVoteResponse(
                        vote.id,
                        vote.voteImage.imageUrl,
                        voteBookMark.createdAt
                ))
                .from(vote)
                .innerJoin(voteBookMark)
                .on(vote.id.eq(voteBookMark.vote.id))
                .where(
                        voteBookMark.bookmaker.id.eq(memberId),
                        ltId(memberBookmarkVotePageRequest.getCursorId())
                )
                .orderBy(
                        vote.id.desc()
                )
                .limit(memberBookmarkVotePageRequest.getSize() + 1)
                .fetch();

        final boolean hasNext = isHasNext(memberBookmarkVoteResponses, memberBookmarkVotePageRequest.getSize());
        return MemberBookmarkVotePageResponse.of(hasNext, memberBookmarkVoteResponses);
    }

    private boolean isHasNext(List<?> result, final int pageSize) {
        boolean hasNext = false;
        if (result.size() > pageSize) {
            hasNext = true;
            result.remove(pageSize);
        }
        return hasNext;
    }

    private BooleanExpression ltId(final Long cursorId) {
        return cursorId == null ? null : vote.id.lt(cursorId);
    }

    private OrderSpecifier[] orderSpecifiers(final SearchTypeConstant searchTypeConstant) {
        final List<OrderSpecifier> orderSpecifierList = new ArrayList<>();

        switch (searchTypeConstant) {
            case BEST:
                orderSpecifierList.add(new OrderSpecifier(Order.DESC, vote.likeCount));
                orderSpecifierList.add(new OrderSpecifier(Order.DESC, vote.id));
                break;

            case HOME:
                orderSpecifierList.add(new OrderSpecifier(Order.DESC, vote.id));
                break;
        }

        return orderSpecifierList.toArray(new OrderSpecifier[orderSpecifierList.size()]);
    }

}

package com.salmalteam.salmal.domain.vote;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.salmalteam.salmal.dto.response.vote.QVoteResponse;
import com.salmalteam.salmal.dto.response.vote.VoteResponse;
import lombok.RequiredArgsConstructor;

import static com.salmalteam.salmal.domain.vote.QVote.vote;
import static com.salmalteam.salmal.domain.vote.bookmark.QVoteBookMark.voteBookMark;
import static com.salmalteam.salmal.domain.vote.evaluation.QVoteEvaluation.voteEvaluation;

@RequiredArgsConstructor
public class VoteRepositoryCustomImpl implements VoteRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public VoteResponse search(final Long id, final Long memberId) {

        final BooleanExpression bookmarkSubQuery = JPAExpressions.selectFrom(voteBookMark)
                .where(
                        voteBookMark.vote.id.eq(id),
                        voteBookMark.bookmaker.id.eq(memberId)
                )
                .exists();

        final JPQLQuery<String> voteEvaluationSubQuery = JPAExpressions.select(
                        voteEvaluation.voteEvaluationType.stringValue()
                )
                .where(
                        voteEvaluation.vote.id.eq(id),
                        voteEvaluation.evaluator.id.eq(memberId)
                )
                .from(voteEvaluation);

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
}

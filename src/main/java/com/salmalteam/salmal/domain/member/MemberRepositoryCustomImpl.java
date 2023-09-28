package com.salmalteam.salmal.domain.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.salmalteam.salmal.dto.response.member.MyPageResponse;
import com.salmalteam.salmal.dto.response.member.QMyPageResponse;
import lombok.RequiredArgsConstructor;

import static com.salmalteam.salmal.domain.member.QMember.member;
import static com.salmalteam.salmal.domain.member.block.QMemberBlocked.memberBlocked;
import static com.salmalteam.salmal.domain.vote.QVote.vote;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public MyPageResponse searchMyPage(final Long memberId) {

        final BooleanExpression blockedMemberSubQuery = JPAExpressions.selectFrom(memberBlocked)
                .where(
                        memberBlocked.blocker.id.eq(memberId)
                )
                .exists();

        final MyPageResponse myPageResponse = jpaQueryFactory.select(new QMyPageResponse(
                        member.id,
                        member.memberImage.imageUrl,
                        member.nickName.value,
                        member.introduction.value,
                        vote.likeCount.sum(),
                        vote.dislikeCount.sum(),
                        blockedMemberSubQuery
                ))
                .from(member)
                .where(member.id.eq(memberId))
                .leftJoin(vote)
                .on(vote.member.id.eq(memberId))
                .groupBy(member.id)
                .fetchOne();

        return myPageResponse;
    }
}

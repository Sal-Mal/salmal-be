package com.salmalteam.salmal.member.entity;

import static com.querydsl.core.types.dsl.Expressions.*;
import static com.salmalteam.salmal.member.entity.QMember.*;
import static com.salmalteam.salmal.member.entity.QMemberBlocked.*;
import static com.salmalteam.salmal.vote.entity.QVote.*;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.salmalteam.salmal.member.dto.response.MyPageResponse;
import com.salmalteam.salmal.member.dto.response.MyPageV2Response;
import com.salmalteam.salmal.member.dto.response.QMyPageResponse;
import com.salmalteam.salmal.member.dto.response.QMyPageV2Response;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public MyPageResponse searchMyPage(final Long memberId) {

		final BooleanExpression blockedMemberSubQuery = JPAExpressions.selectFrom(memberBlocked)
			.where(
				memberBlocked.target.id.eq(memberId)
			)
			.exists();
		return jpaQueryFactory.select(new QMyPageResponse(
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
	}

	@Override
	public MyPageV2Response searchMyPageV2(final Long memberId, Long searchMemberId) {
		return jpaQueryFactory.select(
				new QMyPageV2Response(member.id,
					member.memberImage.imageUrl,
					member.nickName.value,
					member.introduction.value,
					vote.likeCount.sum().as("likeCount"),
					vote.dislikeCount.sum().as("dislikeCount"),
					asBoolean(existsBlockMember(memberId, searchMemberId)),
					vote.id.count().as("totalCount")))
			.from(member)
			.leftJoin(vote)
			.on(vote.member.id.eq(searchMemberId))
			.groupBy(member.id)
			.where(member.id.eq(searchMemberId))
			.fetchOne();
	}

	private boolean existsBlockMember(Long blockerId, Long targetId) {
		return jpaQueryFactory.select(memberBlocked.id)
			.from(memberBlocked)
			.where(memberBlocked.blocker.id.eq(blockerId), memberBlocked.target.id.eq(targetId))
			.fetchFirst() != null;
	}
}

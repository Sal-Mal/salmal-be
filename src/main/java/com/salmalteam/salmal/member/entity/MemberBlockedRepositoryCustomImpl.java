package com.salmalteam.salmal.member.entity;

import static com.salmalteam.salmal.member.entity.QMember.*;
import static com.salmalteam.salmal.member.entity.QMemberBlocked.*;

import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.salmalteam.salmal.member.dto.request.block.MemberBlockedPageRequest;
import com.salmalteam.salmal.member.dto.response.block.MemberBlockedPageResponse;
import com.salmalteam.salmal.member.dto.response.block.MemberBlockedResponse;
import com.salmalteam.salmal.member.dto.response.block.QMemberBlockedResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberBlockedRepositoryCustomImpl implements MemberBlockedRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public MemberBlockedPageResponse searchList(final Long memberId,
		final MemberBlockedPageRequest memberBlockedPageRequest) {

		final List<MemberBlockedResponse> memberBlockedResponses = jpaQueryFactory.select(new QMemberBlockedResponse(
				member.id,
				member.nickName.value,
				member.memberImage.imageUrl,
				member.createAt
			))
			.from(memberBlocked)
			.leftJoin(member)
			.on(member.id.eq(memberBlocked.target.id))
			.where(
				memberBlocked.blocker.id.eq(memberId),
				cursorId(memberBlockedPageRequest.getCursorId())
			)
			.orderBy(memberBlocked.id.desc())
			.limit(memberBlockedPageRequest.getSize() + 1)
			.fetch();

		final boolean hasNext = isHasNext(memberBlockedResponses, memberBlockedPageRequest.getSize());
		return MemberBlockedPageResponse.of(hasNext, memberBlockedResponses);
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
		return cursorId == null ? null : member.id.lt(cursorId);
	}
}

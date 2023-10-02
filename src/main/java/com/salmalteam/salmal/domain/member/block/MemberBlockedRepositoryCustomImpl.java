package com.salmalteam.salmal.domain.member.block;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.salmalteam.salmal.dto.request.member.block.MemberBlockedPageRequest;
import com.salmalteam.salmal.dto.response.member.block.MemberBlockedPageResponse;
import com.salmalteam.salmal.dto.response.member.block.MemberBlockedResponse;
import com.salmalteam.salmal.dto.response.member.block.QMemberBlockedResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.salmalteam.salmal.domain.member.QMember.member;
import static com.salmalteam.salmal.domain.member.block.QMemberBlocked.memberBlocked;

@RequiredArgsConstructor
public class MemberBlockedRepositoryCustomImpl implements MemberBlockedRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public MemberBlockedPageResponse searchList(final Long memberId, final MemberBlockedPageRequest memberBlockedPageRequest) {

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

    private BooleanExpression cursorId(final Long cursorId){
        return cursorId == null ? null : member.id.lt(cursorId);
    }
}

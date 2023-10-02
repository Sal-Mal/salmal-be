package com.salmalteam.salmal.domain.member.block;

import com.salmalteam.salmal.dto.request.member.block.MemberBlockedPageRequest;
import com.salmalteam.salmal.dto.response.member.block.MemberBlockedPageResponse;

public interface MemberBlockedRepositoryCustom {
    MemberBlockedPageResponse searchList(final Long memberId, final MemberBlockedPageRequest memberBlockedPageRequest);
}

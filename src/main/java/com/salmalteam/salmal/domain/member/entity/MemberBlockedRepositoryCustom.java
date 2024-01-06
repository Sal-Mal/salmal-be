package com.salmalteam.salmal.domain.member.entity;

import com.salmalteam.salmal.domain.member.dto.request.block.MemberBlockedPageRequest;
import com.salmalteam.salmal.domain.member.dto.response.block.MemberBlockedPageResponse;

public interface MemberBlockedRepositoryCustom {
    MemberBlockedPageResponse searchList(final Long memberId, final MemberBlockedPageRequest memberBlockedPageRequest);
}

package com.salmalteam.salmal.member.entity;

import com.salmalteam.salmal.member.dto.request.block.MemberBlockedPageRequest;
import com.salmalteam.salmal.member.dto.response.block.MemberBlockedPageResponse;

public interface MemberBlockedRepositoryCustom {
    MemberBlockedPageResponse searchList(final Long memberId, final MemberBlockedPageRequest memberBlockedPageRequest);
}

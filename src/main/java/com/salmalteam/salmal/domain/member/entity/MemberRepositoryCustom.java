package com.salmalteam.salmal.domain.member.entity;

import com.salmalteam.salmal.domain.member.dto.response.MyPageResponse;

public interface MemberRepositoryCustom {
    MyPageResponse searchMyPage(final Long memberId);
}

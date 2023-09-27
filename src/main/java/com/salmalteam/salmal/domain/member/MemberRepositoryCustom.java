package com.salmalteam.salmal.domain.member;

import com.salmalteam.salmal.dto.response.member.MyPageResponse;

public interface MemberRepositoryCustom {
    MyPageResponse searchMyPage(final Long memberId);
}

package com.salmalteam.salmal.member.entity;

import com.salmalteam.salmal.member.dto.response.MyPageResponse;

public interface MemberRepositoryCustom {
    MyPageResponse searchMyPage(final Long memberId);
}

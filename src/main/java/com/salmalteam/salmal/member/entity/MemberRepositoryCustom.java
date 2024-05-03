package com.salmalteam.salmal.member.entity;

import com.salmalteam.salmal.member.dto.response.MyPageResponse;
import com.salmalteam.salmal.member.dto.response.MyPageV2Response;

public interface MemberRepositoryCustom {
    MyPageResponse searchMyPage(final Long memberId);

    MyPageV2Response searchMyPageV2(final Long memberId, Long searchMemberId);
}

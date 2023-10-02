package com.salmalteam.salmal.dto.response.member.block;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberBlockedResponse {
    private Long id;
    private String nickName;
    private String imageUrl;
    private LocalDateTime blockedDate;

    @QueryProjection
    public MemberBlockedResponse(Long id, String nickName, String imageUrl, LocalDateTime blockedDate) {
        this.id = id;
        this.nickName = nickName;
        this.imageUrl = imageUrl;
        this.blockedDate = blockedDate;
    }
}

package com.salmalteam.salmal.member.dto.response.vote;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberVoteResponse {
    private Long id;
    private String imageUrl;
    private LocalDateTime createdDate;

    @QueryProjection
    public MemberVoteResponse(Long id, String imageUrl, LocalDateTime createdDate) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.createdDate = createdDate;
    }
}

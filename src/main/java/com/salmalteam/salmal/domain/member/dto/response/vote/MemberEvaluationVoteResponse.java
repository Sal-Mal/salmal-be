package com.salmalteam.salmal.domain.member.dto.response.vote;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberEvaluationVoteResponse {
    private Long id;
    private String imageUrl;
    private LocalDateTime createdDate;

    @QueryProjection
    public MemberEvaluationVoteResponse(Long id, String imageUrl, LocalDateTime createdDate) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.createdDate = createdDate;
    }
}

package com.salmalteam.salmal.domain.vote.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class VoteResponse {

    private static final String NONE = "NONE";

    private Long id;
    private Long memberId;
    private String imageUrl;
    private String nickName;
    private String memberImageUrl;
    private int commentCount;
    private int likeCount;
    private int disLikeCount;
    private int totalEvaluationCnt;
    private BigDecimal likeRatio;
    private BigDecimal disLikeRatio;
    private LocalDateTime createdAt;
    private boolean isBookmarked;
    private String status;

    @QueryProjection
    public VoteResponse(Long id, Long memberId, String imageUrl, String nickName, String memberImageUrl, int commentCount, int likeCount, int disLikeCount, int totalEvaluationCnt, BigDecimal likeRatio, BigDecimal disLikeRatio, LocalDateTime createdAt, boolean isBookmarked, String status) {
        this.id = id;
        this.memberId = memberId;
        this.imageUrl = imageUrl;
        this.nickName = nickName;
        this.memberImageUrl = memberImageUrl;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.disLikeCount = disLikeCount;
        this.totalEvaluationCnt = totalEvaluationCnt;
        this.likeRatio = likeRatio;
        this.disLikeRatio = disLikeRatio;
        this.createdAt = createdAt;
        this.isBookmarked = isBookmarked;
        this.status = status == null ? NONE : status;
    }
}

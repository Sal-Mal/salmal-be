package com.salmalteam.salmal.dto.response.comment;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReplyResponse {
    private Long id;
    private Long memberId;
    private String nickName;
    private String memberImageUrl;
    private boolean liked;
    private int likeCount;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @QueryProjection
    public ReplyResponse(Long id, Long memberId, String nickName, String memberImageUrl, boolean liked, int likeCount, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.nickName = nickName;
        this.memberImageUrl = memberImageUrl;
        this.liked = liked;
        this.likeCount = likeCount;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}

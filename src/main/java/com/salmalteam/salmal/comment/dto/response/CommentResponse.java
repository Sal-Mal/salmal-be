package com.salmalteam.salmal.comment.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {

    private Long id;
    private Long memberId;
    private String nickName;
    private String memberImageUrl;
    private boolean liked;
    private int likeCount;
    private int replyCount;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @QueryProjection
    public CommentResponse(Long id, Long memberId, String nickName, String memberImageUrl, boolean liked, int likeCount, int replyCount, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.nickName = nickName;
        this.memberImageUrl = memberImageUrl;
        this.liked = liked;
        this.likeCount = likeCount;
        this.replyCount = replyCount;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}

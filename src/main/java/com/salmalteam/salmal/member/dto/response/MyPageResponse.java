package com.salmalteam.salmal.member.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class MyPageResponse {
    private Long id;
    private String imageUrl;
    private String nickName;
    private String introduction;
    private Integer likeCount;
    private Integer disLikeCount;
    private boolean blocked;

    @QueryProjection
    public MyPageResponse(Long id, String imageUrl, String nickName, String introduction, Integer likeCount, Integer disLikeCount, boolean blocked) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.nickName = nickName;
        this.introduction = introduction;
        this.likeCount = likeCount;
        this.disLikeCount = disLikeCount;
        this.blocked = blocked;
    }
}

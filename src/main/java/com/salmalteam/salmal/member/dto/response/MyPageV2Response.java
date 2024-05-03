package com.salmalteam.salmal.member.dto.response;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyPageV2Response {
	private Long id;
	private String imageUrl;
	private String nickName;
	private String introduction;
	private Integer likeCount;
	private Integer disLikeCount;
	private boolean blocked;
	private Long totalVoteCount;

	@QueryProjection
	public MyPageV2Response(Long id, String imageUrl, String nickName, String introduction, Integer likeCount,
		Integer disLikeCount, boolean blocked, Long totalVoteCount) {
		this.id = id;
		this.imageUrl = imageUrl;
		this.nickName = nickName;
		this.introduction = introduction;
		this.likeCount = likeCount;
		this.disLikeCount = disLikeCount;
		this.blocked = blocked;
		this.totalVoteCount = totalVoteCount;
	}
}

package com.salmalteam.salmal.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReplayCommentDto {
	private final Long commentOwnerId;
	private final Long commenterId;
	private final Long commentId;
	private final String nickName;
	private final String content;

	public boolean isSameCommenter() {
		return commentOwnerId.equals(commenterId);
	}
}

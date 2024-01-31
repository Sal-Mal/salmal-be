package com.salmalteam.salmal.comment.dto.response;

import com.salmalteam.salmal.comment.entity.Comment;
import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.vote.entity.Vote;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReplayCommentDto {
	private final Long commentOwnerId;
	private final Long replyerId;
	private final Long commentId;
	private final String nickName;
	private final String content;
	private final String replyerImageUrl;
	private final String VoteImageUrl;
	private final Long voteId;

	public static ReplayCommentDto createNotificationType(Member replyer, Member commenterOwner, Comment comment,
		Comment reply,
		Vote vote) {
		return new ReplayCommentDto(
			commenterOwner.getId(), //알림 타켓 ID
			replyer.getId(), //대댓글 작성자
			comment.getId(), //대댓글을 작성한 댓글 ID
			replyer.getNickName().getValue(), //대댓글 작성자 ID
			reply.getContent().getValue(),  //대댓글 내용
			replyer.getMemberImage().getImageUrl(), //대댓글 작성자 이미지
			vote.getVoteImage().getImageUrl(), //대댓글 작성한 투표의 이미지
			vote.getId()
		);
	}

	public boolean isSameCommenter() {
		return commentOwnerId.equals(replyerId);
	}
}

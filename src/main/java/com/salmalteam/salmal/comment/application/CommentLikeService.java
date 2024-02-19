package com.salmalteam.salmal.comment.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salmalteam.salmal.comment.entity.Comment;
import com.salmalteam.salmal.comment.entity.CommentRepository;
import com.salmalteam.salmal.comment.entity.like.CommentLike;
import com.salmalteam.salmal.comment.entity.like.CommentLikeRepository;
import com.salmalteam.salmal.comment.exception.CommentException;
import com.salmalteam.salmal.comment.exception.CommentExceptionType;
import com.salmalteam.salmal.comment.exception.like.CommentLikeException;
import com.salmalteam.salmal.comment.exception.like.CommentLikeExceptionType;
import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.member.entity.MemberRepository;
import com.salmalteam.salmal.member.exception.MemberException;
import com.salmalteam.salmal.member.exception.MemberExceptionType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

	private final CommentLikeRepository commentLikeRepository;
	private final CommentRepository commentRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public void likeComment(final Long memberId, final Long commentId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND));

		final Comment comment = getCommentById(commentId);

		validateCommentAlreadyLiked(comment, member);
		final CommentLike commentLike = CommentLike.of(comment, member);
		commentLikeRepository.save(commentLike);
		commentRepository.increaseLikeCount(commentId);

	}

	private void validateCommentAlreadyLiked(final Comment comment, final Member member) {
		if (commentLikeRepository.existsByCommentAndLiker(comment, member)) {
			throw new CommentLikeException(CommentLikeExceptionType.DUPLICATED_LIKE);
		}
	}

	@Transactional
	public void unLikeComment(final Long memberId, final Long commentId) {

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND));

		final Comment comment = getCommentById(commentId);
		validateCommentNotLiked(comment, member);

		commentLikeRepository.deleteByCommentAndLiker(comment, member);
		commentRepository.decreaseLikeCount(commentId);
	}

	private void validateCommentNotLiked(final Comment comment, final Member member) {
		if (!commentLikeRepository.existsByCommentAndLiker(comment, member)) {
			throw new CommentLikeException(CommentLikeExceptionType.NOT_FOUND);
		}
	}


	private Comment getCommentById(final Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND));
	}
}

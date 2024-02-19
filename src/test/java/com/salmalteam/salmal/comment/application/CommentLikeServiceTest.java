package com.salmalteam.salmal.comment.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.salmalteam.salmal.comment.entity.Comment;
import com.salmalteam.salmal.comment.entity.CommentRepository;
import com.salmalteam.salmal.comment.entity.like.CommentLikeRepository;
import com.salmalteam.salmal.comment.exception.CommentException;
import com.salmalteam.salmal.comment.exception.like.CommentLikeException;
import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.member.entity.MemberRepository;
import com.salmalteam.salmal.vote.entity.Vote;

@ExtendWith(MockitoExtension.class)
class CommentLikeServiceTest {

	@InjectMocks
	CommentLikeService commentLikeService;
	@Mock
	CommentLikeRepository commentLikeRepository;
	@Mock
	CommentRepository commentRepository;
	@Mock
	MemberRepository memberRepository;

	@Nested
	class 댓글_좋아요_테스트 {

		@Test
		void 좋아요할_댓글이_존재하지_않으면_예외가_발생한다() {
			// given
			final Long memberId = 1L;
			final Long commentId = 1L;
			given(memberRepository.findById(anyLong())).willReturn(
				Optional.ofNullable(Member.createActivatedMember("providerId", "test", "KAKAO", true)));
			given(commentRepository.findById(anyLong())).willReturn(Optional.empty());


			// when & then
			assertThatThrownBy(() -> commentLikeService.likeComment(memberId, commentId))
				.isInstanceOf(CommentException.class);

			verify(memberRepository).findById(eq(memberId));
			then(memberRepository).should(times(1)).findById(anyLong());
			then(commentRepository).should(times(1)).findById(anyLong());

		}

		@Test
		void 이미_좋아요를_한_경우_예외가_발생한다() {

			// given
			final Long memberId = 1L;
			final Long commentId = 1L;

			final Member member = Member.createActivatedMember("xxx", "닉네임", "kakao", true);
			final Vote vote = Vote.of("imageUrl", member);
			final Comment comment = Comment.of("댓글", vote, member);

			given(memberRepository.findById(any())).willReturn(Optional.of(member));
			given(commentRepository.findById(any())).willReturn(Optional.of(comment));
			given(commentLikeRepository.existsByCommentAndLiker(any(), any())).willReturn(true);

			// when & then
			assertThatThrownBy(() -> commentLikeService.likeComment(memberId, commentId))
				.isInstanceOf(CommentLikeException.class);

		}

	}

	@Nested
	class 댓글_좋아요_취소_테스트 {

		@Test
		void 좋아요_취소할_댓글이_존재하지_않으면_예외가_발생한다() {

			// given
			final Long memberId = 1L;
			final Long commentId = 1L;
			given(memberRepository.findById(anyLong())).willReturn(
				Optional.ofNullable(Member.createActivatedMember("providerId", "test", "KAKAO", true)));
			given(commentRepository.findById(anyLong())).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> commentLikeService.unLikeComment(memberId, commentId))
				.isInstanceOf(CommentException.class);

			then(memberRepository).should(times(1)).findById(anyLong());
			then(commentRepository).should(times(1)).findById(anyLong());
		}

		@Test
		void 좋아요를_한_적이_없을_경우_예외가_발생한다() {

			// given
			final Long memberId = 1L;
			final Long commentId = 1L;

			final Member member = Member.createActivatedMember("xxx", "닉네임", "kakao", true);
			final Vote vote = Vote.of("imageUrl", member);
			final Comment comment = Comment.of("댓글", vote, member);

			given(memberRepository.findById(any())).willReturn(Optional.ofNullable(member));
			given(commentRepository.findById(any())).willReturn(Optional.of(comment));
			given(commentLikeRepository.existsByCommentAndLiker(any(), any())).willReturn(false);

			// when & then
			assertThatThrownBy(() -> commentLikeService.unLikeComment(memberId, commentId))
				.isInstanceOf(CommentLikeException.class);

		}
	}

}
package com.salmalteam.salmal.application.comment;

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

import com.salmalteam.salmal.comment.application.CommentService;
import com.salmalteam.salmal.comment.entity.Comment;
import com.salmalteam.salmal.comment.entity.CommentRepository;
import com.salmalteam.salmal.comment.entity.like.CommentLikeRepository;
import com.salmalteam.salmal.comment.entity.report.CommentReportRepository;
import com.salmalteam.salmal.comment.exception.CommentException;
import com.salmalteam.salmal.comment.exception.like.CommentLikeException;
import com.salmalteam.salmal.comment.exception.report.CommentReportException;
import com.salmalteam.salmal.member.application.MemberService;
import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.member.exception.MemberException;
import com.salmalteam.salmal.member.exception.MemberExceptionType;
import com.salmalteam.salmal.vote.dto.request.VoteCommentUpdateRequest;
import com.salmalteam.salmal.vote.entity.Vote;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

	@InjectMocks
	CommentService commentService;
	@Mock
	MemberService memberService;
	@Mock
	CommentRepository commentRepository;
	@Mock
	CommentLikeRepository commentLikeRepository;
	@Mock
	CommentReportRepository commentReportRepository;

	@Nested
	class 댓글_수정_테스트 {

		@Test
		void 요청_회원이_존재하지_않는다면_예외가_발생한다() {
			// given
			final Long memberId = 1L;
			final Long commentId = 1L;
			final String content = "수정할 댓글입니다";
			final VoteCommentUpdateRequest voteCommentUpdateRequest = new VoteCommentUpdateRequest(content);

			given(memberService.findMemberById(any())).willThrow(new MemberException(MemberExceptionType.NOT_FOUND));
			// when & then
			assertThatThrownBy(() -> commentService.updateComment(memberId, commentId, voteCommentUpdateRequest))
				.isInstanceOf(MemberException.class);
		}

		@Test
		void 수정할_댓글이_존재하지_않는다면_예외가_발생한다() {
			// given
			final Long memberId = 1L;
			final Long commentId = 1L;
			final String content = "수정할 댓글입니다";
			final VoteCommentUpdateRequest voteCommentUpdateRequest = new VoteCommentUpdateRequest(content);
			final Member member = Member.createActivatedMember("LLLLLLL", "닉네임", "KAKAO", true);

			given(memberService.findMemberById(any())).willReturn(member);
			given(commentRepository.findById(any())).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> commentService.updateComment(memberId, commentId, voteCommentUpdateRequest))
				.isInstanceOf(CommentException.class);

		}
	}

	@Nested
	class 댓글_좋아요_테스트 {

		@Test
		void 좋아요할_댓글이_존재하지_않으면_예외가_발생한다() {
			// given
			final Long memberId = 1L;
			final Long commentId = 1L;

			// when & then
			assertThatThrownBy(() -> commentService.likeComment(memberId, commentId))
				.isInstanceOf(CommentException.class);

			verify(memberService).findMemberById(eq(memberId));
		}

		@Test
		void 이미_좋아요를_한_경우_예외가_발생한다() {

			// given
			final Long memberId = 1L;
			final Long commentId = 1L;

			final Member member = Member.createActivatedMember("xxx", "닉네임", "kakao", true);
			final Vote vote = Vote.of("imageUrl", member);
			final Comment comment = Comment.of("댓글", vote, member);

			given(memberService.findMemberById(any())).willReturn(member);
			given(commentRepository.findById(any())).willReturn(Optional.of(comment));
			given(commentLikeRepository.existsByCommentAndLiker(any(), any())).willReturn(true);

			// when & then
			assertThatThrownBy(() -> commentService.likeComment(memberId, commentId))
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

			// when & then
			assertThatThrownBy(() -> commentService.unLikeComment(memberId, commentId))
				.isInstanceOf(CommentException.class);

			verify(memberService).findMemberById(eq(memberId));
		}

		@Test
		void 좋아요를_한_적이_없을_경우_예외가_발생한다() {

			// given
			final Long memberId = 1L;
			final Long commentId = 1L;

			final Member member = Member.createActivatedMember("xxx", "닉네임", "kakao", true);
			final Vote vote = Vote.of("imageUrl", member);
			final Comment comment = Comment.of("댓글", vote, member);

			given(memberService.findMemberById(any())).willReturn(member);
			given(commentRepository.findById(any())).willReturn(Optional.of(comment));
			given(commentLikeRepository.existsByCommentAndLiker(any(), any())).willReturn(false);

			// when & then
			assertThatThrownBy(() -> commentService.unLikeComment(memberId, commentId))
				.isInstanceOf(CommentLikeException.class);

		}
	}

	@Nested
	class 댓글_신고_테스트 {

		@Test
		void 신고할_댓글이_존재하지_않을_경우_예외가_발생한다() {
			// given
			final Long memberId = 1L;
			final Long commentId = 1L;

			// when & then
			assertThatThrownBy(() -> commentService.report(memberId, commentId))
				.isInstanceOf(CommentException.class);

			verify(memberService).findMemberById(eq(memberId));
		}

		@Test
		void 이미_신고를_한_경우_예외가_발생한다() {
			// given
			final Long memberId = 1L;
			final Long commentId = 1L;

			final Member member = Member.createActivatedMember("ss", "닉네임", "kakao", true);
			final Vote vote = Vote.of("imageUrl", member);
			final Comment comment = Comment.of("내용", vote, member);
			given(memberService.findMemberById(eq(memberId))).willReturn(member);
			given(commentRepository.findById(eq(commentId))).willReturn(Optional.of(comment));
			given(commentReportRepository.existsByCommentAndReporter(any(), any())).willReturn(true);

			// when & then
			assertThatThrownBy(() -> commentService.report(memberId, commentId))
				.isInstanceOf(CommentReportException.class);

		}
	}

	// @Nested
	// class 댓글_대댓글_조회_테스트 {
	// 	@Test
	// 	@DisplayName("댓글 ID 를 참조하는 대댓글이 조회 되어야 한다.")
	// 	void searchReplies() {
	// 		//given
	// 		final Long memberId = 1L;
	// 		long commentId = 10L;
	// 		final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);
	// 		ReplyPageRequest pageRequest = ReplyPageRequest.of(10L, 5);
	//
	// 		ArrayList<ReplyResponse> replyResponses = new ArrayList<>();
	// 		replyResponses.add(
	// 			createReply(11L, 100L, "testNick1", "testUrl1", true, 0, "test content1"));
	// 		replyResponses.add(
	// 			createReply(12L, 100L, "testNick2", "testUrl2", true, 12, "test content2"));
	// 		replyResponses.add(
	// 			createReply(13L, 3214L, "testNick3", "testUrl3", true, 13, "test content3"));
	// 		replyResponses.add(
	// 			createReply(14L, 50124L, "testNick4", "testUrl4", true, 15, "test content4"));
	// 		replyResponses.add(
	// 			createReply(15L, 12312050L, "testNick5", "testUrl5", true, 1, "test content5"));
	//
	// 		ReplyPageResponse replyPageResponse = ReplyPageResponse.of(true, replyResponses);
	//
	// 		given(commentRepository.existsById(anyLong())).willReturn(true);
	// 		given(memberService.findMemberById(anyLong())).willReturn(
	// 			Member.createActivatedMember("testProviderId", "member", "APPLE", true));
	// 		given(memberService.findBlockedMembers(anyLong())).willReturn(Collections.emptyList());
	// 		given(commentRepository.searchReplies(anyLong(), anyLong(), any(ReplyPageRequest.class)))
	// 			.willReturn(replyPageResponse);
	//
	// 		//when
	// 		ReplyPageResponse actual = commentService.searchReplies(memberPayLoad, commentId, pageRequest);
	//
	// 		//then
	// 		assertThat(actual.getComments()).hasSize(5);
	//
	// 		then(commentRepository).should(times(1)).existsById(anyLong());
	// 		then(memberService).should(times(1)).findMemberById(anyLong());
	// 		then(memberService).should(times(1)).findBlockedMembers(anyLong());
	// 		then(commentRepository).should(times(1)).searchReplies(anyLong(), anyLong(), any(ReplyPageRequest.class));
	// 	}
	//
	// 	private ReplyResponse createReply(
	// 		long id, long memberId, String nickName, String imageUrl, boolean liked, int likeCount, String content) {
	// 		return new ReplyResponse(id, memberId, nickName, imageUrl, liked, likeCount, content, LocalDateTime.now(),
	// 			LocalDateTime.now());
	// 	}
	// }

}
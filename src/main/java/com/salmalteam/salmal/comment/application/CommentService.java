package com.salmalteam.salmal.comment.application;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salmalteam.salmal.comment.dto.response.ReplayCommentDto;
import com.salmalteam.salmal.comment.exception.like.CommentLikeException;
import com.salmalteam.salmal.comment.exception.like.CommentLikeExceptionType;
import com.salmalteam.salmal.comment.exception.report.CommentReportException;
import com.salmalteam.salmal.comment.exception.report.CommentReportExceptionType;
import com.salmalteam.salmal.member.application.MemberService;
import com.salmalteam.salmal.comment.entity.Comment;
import com.salmalteam.salmal.comment.entity.CommentRepository;
import com.salmalteam.salmal.comment.entity.CommentType;
import com.salmalteam.salmal.comment.entity.like.CommentLike;
import com.salmalteam.salmal.comment.entity.like.CommentLikeRepository;
import com.salmalteam.salmal.comment.entity.report.CommentReport;
import com.salmalteam.salmal.comment.entity.report.CommentReportRepository;
import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.vote.entity.Vote;
import com.salmalteam.salmal.vote.entity.VoteRepository;
import com.salmalteam.salmal.comment.dto.request.CommentPageRequest;
import com.salmalteam.salmal.comment.dto.request.CommentReplyCreateRequest;
import com.salmalteam.salmal.comment.dto.request.ReplyPageRequest;
import com.salmalteam.salmal.vote.dto.request.VoteCommentUpdateRequest;
import com.salmalteam.salmal.comment.dto.response.CommentPageResponse;
import com.salmalteam.salmal.comment.dto.response.CommentResponse;
import com.salmalteam.salmal.comment.dto.response.ReplyPageResponse;
import com.salmalteam.salmal.comment.dto.response.ReplyResponse;
import com.salmalteam.salmal.comment.exception.CommentException;
import com.salmalteam.salmal.comment.exception.CommentExceptionType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final MemberService memberService;
	private final CommentRepository commentRepository;
	private final CommentReportRepository commentReportRepository;
	private final CommentLikeRepository commentLikeRepository;
	private final VoteRepository voteRepository;

	@Transactional
	public void save(final String content, final Vote vote, final Member member) {
		final Comment comment = Comment.of(content, vote, member);
		commentRepository.save(comment);
	}

	@Transactional
	public void deleteComment(final Long memberId, final Long commentId) {
		final Comment comment = getCommentById(commentId);
		validateDeleteAuthority(comment.getCommenter().getId(), memberId);

		switch (comment.getCommentType()) {
			case COMMENT:
				commentRepository.deleteAllRepliesByParentCommentId(commentId);
				voteRepository.decreaseCommentCount(comment.getVote().getId());
			case REPLY:
				commentRepository.decreaseReplyCount(commentId);
		}
		commentRepository.delete(comment);
	}

	private void validateDeleteAuthority(final Long commenterId, final Long requesterId) {
		if (commenterId != requesterId) {
			throw new CommentException(CommentExceptionType.FORBIDDEN_DELETE);
		}
	}

	@Transactional
	public ReplayCommentDto replyComment(final Long memberId, final Long commentId,
		final CommentReplyCreateRequest commentReplyCreateRequest) {
		final Member replyer = memberService.findMemberById(memberId); //대댓글 작성자
		final Comment comment = getCommentById(commentId); //대댓글을 작성한 댓글(대댓글 주인)
		final Comment reply = Comment.ofReply(commentReplyCreateRequest.getContent(), comment, replyer); //대댓글
		final Member commenterOwner = comment.getCommenter(); //댓글 주인
		commentRepository.save(reply);
		commentRepository.increaseReplyCount(commentId);

		return ReplayCommentDto.createNotificationType(replyer, commenterOwner, comment, reply, comment.getVote());
    }

	@Transactional(readOnly = true)
	public ReplyPageResponse searchReplies(final Long memberId, final Long commentId,
		final ReplyPageRequest replyPageRequest) {
		validateCommentExist(commentId);
		final Member member = memberService.findMemberById(memberId);
		List<Long> ids = memberService.findBlockedMembers(member.getId());
		ReplyPageResponse replyPageResponse = commentRepository.searchReplies(commentId, member.getId(),
			replyPageRequest);
		replyPageResponse.filteringBlockedMembers(ids);
		return replyPageResponse;
	}

	@Transactional(readOnly = true)
	public List<ReplyResponse> searchAllReplies(final Long memberId, final Long commentId) {
		validateCommentExist(commentId);
		final Member member = memberService.findMemberById(memberId);
		List<Long> ids = memberService.findBlockedMembers(member.getId());
		List<ReplyResponse> replyResponses = commentRepository.searchAllReplies(commentId, member.getId());
		return filteringBlockedMembers(replyResponses, ids);
	}

	public List<ReplyResponse> filteringBlockedMembers(List<ReplyResponse> commentResponses, List<Long> ids) {
		return commentResponses.stream()
			.filter(filterBlockedMemberPredicate(ids))
			.collect(toList());
	}

	private Predicate<ReplyResponse> filterBlockedMemberPredicate(List<Long> ids) {
		return voteResponse -> ids.stream()
			.noneMatch(id -> id.equals(voteResponse.getMemberId()));
	}

	private void validateCommentExist(final Long commentId) {
		if (!commentRepository.existsById(commentId)) {
			throw new CommentException(CommentExceptionType.NOT_FOUND);
		}
	}

	@Transactional
	public void updateComment(final Long memberId, final Long commentId,
		final VoteCommentUpdateRequest voteCommentUpdateRequest) {

		final Member member = memberService.findMemberById(memberId);
		final Comment comment = getCommentById(commentId);
		final String content = voteCommentUpdateRequest.getContent();
		validateAuthority(comment, member);

		comment.updateContent(content);

		commentRepository.save(comment);
	}

	private void validateAuthority(final Comment comment, final Member member) {
		if (comment.getCommenter().getId() != member.getId()) {
			throw new CommentException(CommentExceptionType.FORBIDDEN_UPDATE);
		}
	}

	@Transactional(readOnly = true)
	public CommentPageResponse searchList(final Long voteId, final Long memberId,
		final CommentPageRequest commentPageRequest) {
		return commentRepository.searchList(voteId, memberId, commentPageRequest);
	}

	@Transactional(readOnly = true)
	public List<CommentResponse> searchAllList(final Long voteId, final Long memberId) {
		return commentRepository.searchAllList(voteId, memberId);
	}

	@Transactional
	public void likeComment(final Long memberId, final Long commentId) {

		final Member member = memberService.findMemberById(memberId);
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

		final Member member = memberService.findMemberById(memberId);
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

	@Transactional
	public void report(final Long memberId, final Long commentId) {

		final Member member = memberService.findMemberById(memberId);
		final Comment comment = getCommentById(commentId);
		final CommentReport commentReport = CommentReport.of(comment, member);

		validateCommentAlreadyReport(comment, member);

		commentReportRepository.save(commentReport);
	}

	private void validateCommentAlreadyReport(final Comment comment, final Member member) {
		if (commentReportRepository.existsByCommentAndReporter(comment, member)) {
			throw new CommentReportException(CommentReportExceptionType.DUPLICATED_REPORT);
		}
	}

	private Comment getCommentById(final Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new CommentException(CommentExceptionType.NOT_FOUND));
	}

	/**
	 * 회원 삭제 이벤트 : 댓글의 대댓글 개수 감소
	 */
	@Transactional
	public void decreaseReplyCountByMemberDelete(final Long memberId) {

		// 회원이 작성한 대댓글 목록 조회
		final List<Comment> replies = commentRepository.findALlByCommenter_idAndCommentType(memberId,
			CommentType.REPLY);

		// parent_id 기준으로 Comment 묶기
		Map<Comment, List<Comment>> commentListMap = replies.stream()
			.collect(Collectors.groupingBy(Comment::getParentComment));

		// 대댓글 개수 감소
		commentListMap.forEach((comment, replyList) -> {
			comment.decreaseReplyCount(replyList.size());
		});

	}

	//    /**
	//     * 회원 삭제 이벤트 : 댓글의 좋아요 개수 감소
	//     */
	//    @Transactional
	//    public void decreaseLikeCountByMemberDelete(final Long memberId){
	//        // 회원이 작성한 좋아요 목록 조회
	//        final List<CommentLike> commentLikes = commentLikeRepository.findAllByLiker_Id(memberId);
	//
	//        // vote 기준으로 좋아요 묶기
	//        Map<Comment, List<CommentLike>> commentListMap = commentLikes.stream()
	//                .collect(Collectors.groupingBy(CommentLike::getComment));
	//
	//        // 좋아요 개수 감소
	//        commentListMap.forEach((comment, commentLikeList) -> {
	//            comment.decreaseLikeCount(commentLikeList.size());
	//        });
	//    }

}

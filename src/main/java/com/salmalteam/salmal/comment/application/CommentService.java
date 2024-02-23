package com.salmalteam.salmal.comment.application;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salmalteam.salmal.comment.dto.request.CommentPageRequest;
import com.salmalteam.salmal.comment.dto.request.CommentReplyCreateRequest;
import com.salmalteam.salmal.comment.dto.request.ReplyPageRequest;
import com.salmalteam.salmal.comment.dto.response.CommentPageResponse;
import com.salmalteam.salmal.comment.dto.response.CommentResponse;
import com.salmalteam.salmal.comment.dto.response.ReplayCommentDto;
import com.salmalteam.salmal.comment.dto.response.ReplyPageResponse;
import com.salmalteam.salmal.comment.dto.response.ReplyResponse;
import com.salmalteam.salmal.comment.entity.Comment;
import com.salmalteam.salmal.comment.entity.CommentRepository;
import com.salmalteam.salmal.comment.entity.report.CommentReport;
import com.salmalteam.salmal.comment.entity.report.CommentReportRepository;
import com.salmalteam.salmal.comment.exception.CommentException;
import com.salmalteam.salmal.comment.exception.CommentExceptionType;
import com.salmalteam.salmal.comment.exception.report.CommentReportException;
import com.salmalteam.salmal.comment.exception.report.CommentReportExceptionType;
import com.salmalteam.salmal.member.application.MemberService;
import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.vote.dto.request.VoteCommentUpdateRequest;
import com.salmalteam.salmal.vote.entity.Vote;
import com.salmalteam.salmal.vote.entity.VoteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final MemberService memberService;
	private final CommentRepository commentRepository;
	private final CommentReportRepository commentReportRepository;
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
		if (comment.isComment()) {
			long deleteComment = commentRepository.countByParentComment(comment) + 1;
			Vote vote = comment.getVote();
			vote.decreaseCommentCount(Math.toIntExact(deleteComment));
			commentRepository.deleteAllRepliesByParentCommentId(commentId);
			commentRepository.delete(comment);
			return;
		}

		Comment parentComment = comment.getParentComment();
		commentRepository.decreaseReplyCount(commentId);
		voteRepository.decreaseCommentCount(parentComment.getVote().getId());
		commentRepository.delete(comment);
	}

	private void validateDeleteAuthority(final Long commenterId, final Long requesterId) {
		if (!Objects.equals(commenterId, requesterId)) {
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
		Vote vote = comment.getVote();
		Long voteId = vote.getId();
		String imageUrl = vote.getVoteImage().getImageUrl();
		voteRepository.increaseCommentCount(vote.getId());
		commentRepository.save(reply);
		commentRepository.increaseReplyCount(commentId);
		return ReplayCommentDto.createNotificationType(replyer, commenterOwner, comment, reply, imageUrl, voteId);
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
		if (!Objects.equals(comment.getCommenter().getId(), member.getId())) {
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
}

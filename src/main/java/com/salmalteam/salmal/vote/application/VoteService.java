package com.salmalteam.salmal.vote.application;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.salmalteam.salmal.comment.application.CommentService;
import com.salmalteam.salmal.comment.dto.request.CommentPageRequest;
import com.salmalteam.salmal.comment.dto.response.CommentPageResponse;
import com.salmalteam.salmal.comment.dto.response.CommentResponse;
import com.salmalteam.salmal.comment.entity.CommentRepository;
import com.salmalteam.salmal.image.application.ImageUploader;
import com.salmalteam.salmal.image.entity.ImageFile;
import com.salmalteam.salmal.member.application.MemberService;
import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.presentation.http.vote.SearchTypeConstant;
import com.salmalteam.salmal.vote.dto.request.VoteCommentCreateRequest;
import com.salmalteam.salmal.vote.dto.request.VoteCreateRequest;
import com.salmalteam.salmal.vote.dto.request.VotePageRequest;
import com.salmalteam.salmal.vote.dto.response.VotePageResponse;
import com.salmalteam.salmal.vote.dto.response.VoteResponse;
import com.salmalteam.salmal.vote.entity.Vote;
import com.salmalteam.salmal.vote.entity.VoteBookMark;
import com.salmalteam.salmal.vote.entity.VoteBookMarkRepository;
import com.salmalteam.salmal.vote.entity.VoteRepository;
import com.salmalteam.salmal.vote.entity.evaluation.VoteEvaluation;
import com.salmalteam.salmal.vote.entity.evaluation.VoteEvaluationRepository;
import com.salmalteam.salmal.vote.entity.evaluation.VoteEvaluationType;
import com.salmalteam.salmal.vote.entity.report.VoteReport;
import com.salmalteam.salmal.vote.entity.report.VoteReportRepository;
import com.salmalteam.salmal.vote.exception.VoteException;
import com.salmalteam.salmal.vote.exception.VoteExceptionType;
import com.salmalteam.salmal.vote.exception.bookmark.VoteBookmarkException;
import com.salmalteam.salmal.vote.exception.bookmark.VoteBookmarkExceptionType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VoteService {

	private final MemberService memberService;
	private final VoteRepository voteRepository;
	private final VoteEvaluationRepository voteEvaluationRepository;
	private final VoteBookMarkRepository voteBookMarkRepository;
	private final VoteReportRepository voteReportRepository;
	private final CommentService commentService;
	private final CommentRepository commentRepository;
	private final ImageUploader imageUploader;
	private final String voteImagePath;

	public VoteService(final MemberService memberService,
		final VoteRepository voteRepository,
		final VoteEvaluationRepository voteEvaluationRepository,
		final VoteBookMarkRepository voteBookMarkRepository,
		final VoteReportRepository voteReportRepository,
		final CommentService commentService,
		final CommentRepository commentRepository,
		final ImageUploader imageUploader,
		@Value("${image.path.vote}") String voteImagePath) {
		this.memberService = memberService;
		this.voteRepository = voteRepository;
		this.voteEvaluationRepository = voteEvaluationRepository;
		this.voteBookMarkRepository = voteBookMarkRepository;
		this.voteReportRepository = voteReportRepository;
		this.commentService = commentService;
		this.commentRepository = commentRepository;
		this.imageUploader = imageUploader;
		this.voteImagePath = voteImagePath;
	}

	@Transactional
	public void register(final Long memberId, final VoteCreateRequest voteCreateRequest) {
		final MultipartFile multipartFile = voteCreateRequest.getImageFile();
		final String imageUrl = imageUploader.uploadImage(ImageFile.of(multipartFile, voteImagePath));
		final Member member = memberService.findMemberById(memberId);
		voteRepository.save(Vote.of(imageUrl, member));
	}

	/**
	 * TODO: 비동기로 S3 에 올라가있는 투표 이미지 삭제하기
	 */
	@Transactional
	public void delete(final Long requesterId, final Long voteId) {
		final Vote vote = getVoteById(voteId);
		final Long writerId = vote.getMember().getId();
		validateDeleteAuthority(writerId, requesterId);

		voteRepository.delete(vote);
	}

	private void validateDeleteAuthority(final Long writerId, final Long requesterId) {
		if (writerId == null || !Objects.equals(writerId, requesterId)) {
			throw new VoteException(VoteExceptionType.FORBIDDEN_DELETE);
		}
	}

	@Transactional
	public void evaluate(final Long memberId, final Long voteId,
		final VoteEvaluationType voteEvaluationType) {

		final Member member = memberService.findMemberById(memberId);
		final Vote vote = getVoteById(voteId);

		validateEvaluationVoteDuplicated(member, vote, voteEvaluationType);
		deleteExistsEvaluation(member, vote);

		switch (voteEvaluationType) {
			case LIKE:
				voteRepository.updateVoteEvaluationStatisticsForEvaluationLikeInsert(voteId);
				break;
			case DISLIKE:
				voteRepository.updateVoteEvaluationStatisticsForEvaluationDisLikeInsert(voteId);
				break;
		}

		voteEvaluationRepository.save(VoteEvaluation.of(vote, member, voteEvaluationType));
	}

	private void validateEvaluationVoteDuplicated(final Member member, final Vote vote,
		final VoteEvaluationType voteEvaluationType) {
		if (voteEvaluationRepository.existsByEvaluatorAndVoteAndVoteEvaluationType(member, vote, voteEvaluationType)) {
			throw new VoteException(VoteExceptionType.DUPLICATED_VOTE_EVALUATION);
		}
	}

	@Transactional
	public void cancelEvaluation(final Long memberId, final Long voteId) {

		final Member member = memberService.findMemberById(memberId);
		final Vote vote = getVoteById(voteId);

		deleteExistsEvaluation(member, vote);
		voteEvaluationRepository.deleteByEvaluatorAndVote(member, vote);
	}

	private void deleteExistsEvaluation(final Member member, final Vote vote) {

		final Optional<VoteEvaluation> evaluationOptional = voteEvaluationRepository.findByEvaluatorAndVote(member,
			vote);
		if (evaluationOptional.isPresent()) {
			final VoteEvaluation voteEvaluation = evaluationOptional.get();
			final VoteEvaluationType voteEvaluationType = voteEvaluation.getVoteEvaluationType();
			final Long voteId = vote.getId();
			switch (voteEvaluationType) {
				case LIKE:
					voteRepository.updateVoteEvaluationsStatisticsForEvaluationLikeDelete(voteId);
					break;
				case DISLIKE:
					voteRepository.updateVoteEvaluationsStatisticsForEvaluationDisLikeDelete(voteId);
					break;
			}
			voteEvaluationRepository.deleteByEvaluatorAndVote(member, vote);
		}
	}

	@Transactional
	public void bookmark(final Long memberId, final Long voteId) {

		final Member member = memberService.findMemberById(memberId);
		final Vote vote = getVoteById(voteId);

		validateBookmarkExist(vote, member);
		final VoteBookMark voteBookMark = voteBookMarkRepository.findByVoteAndBookmaker(vote, member)
			.orElse(VoteBookMark.of(member, vote));

		voteBookMarkRepository.save(voteBookMark);
	}

	private void validateBookmarkExist(final Vote vote, final Member member) {
		if (voteBookMarkRepository.existsByVoteAndBookmaker(vote, member)) {
			throw new VoteBookmarkException(VoteBookmarkExceptionType.DUPLICATED_BOOKMARK);
		}
	}

	@Transactional
	public void cancelBookmark(final Long memberId, final Long voteId) {

		final Member member = memberService.findMemberById(memberId);
		final Vote vote = getVoteById(voteId);

		voteBookMarkRepository.deleteByVoteAndBookmaker(vote, member);
	}

	@Transactional
	public void report(final Long memberId, final Long voteId) {

		final Member member = memberService.findMemberById(memberId);
		final Vote vote = getVoteById(voteId);

		validateVoteReportDuplicated(vote, member);
		final VoteReport voteReport = VoteReport.of(vote, member);

		voteReportRepository.save(voteReport);
	}

	@Transactional
	public void report(final Long memberId, final Long voteId, final String reason) {

		final Member member = memberService.findMemberById(memberId);
		final Vote vote = getVoteById(voteId);

		validateVoteReportDuplicated(vote, member);
		final VoteReport voteReport = VoteReport.createByReason(vote, member, reason);

		voteReportRepository.save(voteReport);
	}

	private void validateVoteReportDuplicated(final Vote vote, final Member member) {
		if (voteReportRepository.existsByVoteAndReporter(vote, member)) {
			throw new VoteException(VoteExceptionType.DUPLICATED_VOTE_REPORT);
		}
	}

	@Transactional
	public void comment(final Long memberId, final Long voteId,
		final VoteCommentCreateRequest voteCommentCreateRequest) {

		final Member member = memberService.findMemberById(memberId);
		final Vote vote = getVoteById(voteId);
		final String content = voteCommentCreateRequest.getContent();

		voteRepository.increaseCommentCount(voteId);
		commentService.save(content, vote, member);
	}

	@Transactional(readOnly = true)
	public CommentPageResponse searchComments(final Long voteId, final Long memberId,
		final CommentPageRequest commentPageRequest) {
		validateVoteExist(voteId);
		List<Long> ids = memberService.findBlockedMembers(memberId);
		CommentPageResponse commentPageResponse = commentService.searchList(voteId, memberId, commentPageRequest);
		commentPageResponse.filteringBlockedMembers(ids);
		return commentPageResponse;
	}

	@Transactional(readOnly = true)
	public List<CommentResponse> searchAllComments(final Long voteId, final Long memberId) {
		validateVoteExist(voteId);
		List<CommentResponse> commentResponses = commentService.searchAllList(voteId, memberId);
		return filteringBlockedMembers(commentResponses, memberService.findBlockedMembers(memberId));
	}

	private List<CommentResponse> filteringBlockedMembers(List<CommentResponse> commentResponses, List<Long> ids) {
		return commentResponses.stream()
			.filter(filterBlockedMemberPredicate(ids))
			.collect(toList());
	}

	private Predicate<CommentResponse> filterBlockedMemberPredicate(List<Long> ids) {
		return voteResponse -> ids.stream()
			.noneMatch(id -> id.equals(voteResponse.getMemberId()));
	}

	@Transactional(readOnly = true)
	public VoteResponse search(final Long memberId, final Long voteId) {
		validateVoteExist(voteId);
		return voteRepository.search(voteId, memberId);
	}

	private void validateVoteExist(final Long voteId) {
		if (!voteRepository.existsById(voteId)) {
			throw new VoteException(VoteExceptionType.NOT_FOUND);
		}
	}

	private Vote getVoteById(final Long voteId) {
		return voteRepository.findById(voteId)
			.orElseThrow(() -> new VoteException(VoteExceptionType.NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public VotePageResponse searchList(final Long memberId, final VotePageRequest votePageRequest,
		final SearchTypeConstant searchTypeConstant) {
		List<Long> blockedMembers = memberService.findBlockedMembers(memberId);
		List<Long> reportVoteIds = voteReportRepository.findReportVoteIdByMemberId(memberId);
		VotePageResponse votePageResponse = voteRepository.searchList(memberId, votePageRequest, searchTypeConstant);
		votePageResponse.filteringBlockedMembers(blockedMembers);
		votePageResponse.filteringReportVotes(reportVoteIds);
		return votePageResponse;
	}
}

package com.salmalteam.salmal.presentation.http.member;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.salmalteam.salmal.auth.annotation.LoginMember;
import com.salmalteam.salmal.member.application.MemberService;
import com.salmalteam.salmal.member.dto.request.MemberImageUpdateRequest;
import com.salmalteam.salmal.member.dto.request.MyPageUpdateRequest;
import com.salmalteam.salmal.member.dto.request.block.MemberBlockedPageRequest;
import com.salmalteam.salmal.member.dto.request.vote.MemberBookmarkVotePageRequest;
import com.salmalteam.salmal.member.dto.request.vote.MemberEvaluationVotePageRequest;
import com.salmalteam.salmal.member.dto.request.vote.MemberVotePageRequest;
import com.salmalteam.salmal.member.dto.response.MyPageResponse;
import com.salmalteam.salmal.member.dto.response.block.MemberBlockedPageResponse;
import com.salmalteam.salmal.member.dto.response.vote.MemberBookmarkVotePageResponse;
import com.salmalteam.salmal.member.dto.response.vote.MemberEvaluationVotePageResponse;
import com.salmalteam.salmal.member.dto.response.vote.MemberVotePageResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/{member-id}")
	@ResponseStatus(HttpStatus.OK)
	public MyPageResponse findMyPage(@PathVariable("member-id") final Long memberId) {
		return memberService.findMyPage(memberId);
	}

	@PutMapping("/{member-id}")
	@ResponseStatus(HttpStatus.OK)
	public void updateMyPage(@LoginMember Long memberId, @PathVariable("member-id") final Long targetId,
		@RequestBody @Valid MyPageUpdateRequest myPageUpdateRequest) {
		memberService.updateMyPage(memberId, targetId, myPageUpdateRequest);
	}

	@DeleteMapping("/{member-id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteMember(@LoginMember Long memberId, @PathVariable("member-id") final Long targetId) {
		memberService.delete(memberId, targetId);
	}

	@PostMapping("/{member-id}/images")
	@ResponseStatus(HttpStatus.OK)
	public void updateImage(@LoginMember Long memberId, @PathVariable("member-id") final Long targetId,
		@ModelAttribute @Valid MemberImageUpdateRequest memberImageUpdateRequest) {
		memberService.updateImage(memberId, targetId, memberImageUpdateRequest);
	}

	@DeleteMapping("/{member-id}/images")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteImage(@LoginMember Long memberId, @PathVariable("member-id") final Long targetId) {
		memberService.deleteImage(memberId, targetId);
	}

	@PostMapping("/{member-id}/blocks")
	@ResponseStatus(HttpStatus.CREATED)
	public void blockMember(@LoginMember Long memberId, @PathVariable("member-id") final Long targetId) {
		memberService.block(memberId, targetId);
	}

	@DeleteMapping("/{member-id}/blocks")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelBlocking(@LoginMember final Long memberId, @PathVariable("member-id") final Long targetId) {
		memberService.cancelBlocking(memberId, memberId);
	}

	@GetMapping("/{member-id}/blocks")
	@ResponseStatus(HttpStatus.OK)
	public MemberBlockedPageResponse searchBlockMembers(@LoginMember final Long memberId,
		@PathVariable("member-id") final Long targetId,
		@RequestParam(value = "cursor-id", required = false) final Long cursorId,
		@RequestParam(value = "size", required = false) final Integer size) {
		final MemberBlockedPageRequest memberBlockedPageRequest = MemberBlockedPageRequest.of(cursorId, size);
		return memberService.searchBlockedMembers(memberId, targetId, memberBlockedPageRequest);
	}

	@GetMapping("/{member-id}/votes")
	@ResponseStatus(HttpStatus.OK)
	public MemberVotePageResponse searchMemberVotes(@LoginMember final Long memberId,
		@PathVariable("member-id") final Long targetId,
		@RequestParam(value = "cursor-id", required = false) final Long cursorId,
		@RequestParam(value = "size", required = false) final Integer size) {
		final MemberVotePageRequest memberVotePageRequest = MemberVotePageRequest.of(cursorId, size);
		return memberService.searchMemberVotes(memberId, targetId, memberVotePageRequest);
	}

	@GetMapping("/{member-id}/evaluations")
	@ResponseStatus(HttpStatus.OK)
	public MemberEvaluationVotePageResponse searchMemberEvaluationVotes(@LoginMember final Long memberId,
		@PathVariable("member-id") final Long targetId,
		@RequestParam(value = "cursor-id", required = false) final Long cursorId,
		@RequestParam(value = "size", required = false) final Integer size) {
		final MemberEvaluationVotePageRequest memberEvaluationVotePageRequest = MemberEvaluationVotePageRequest.of(
			cursorId, size);
		return memberService.searchMemberEvaluatedVotes(memberId, targetId, memberEvaluationVotePageRequest);
	}

	@GetMapping("/{member-id}/bookmarks")
	@ResponseStatus(HttpStatus.OK)
	public MemberBookmarkVotePageResponse searchMemberBookmarkVotes(@LoginMember final Long memberId,
		@PathVariable("member-id") final Long targetId,
		@RequestParam(value = "cursor-id", required = false) final Long cursorId,
		@RequestParam(value = "size", required = false) final Integer size) {
		final MemberBookmarkVotePageRequest memberBookmarkVotePageRequest = MemberBookmarkVotePageRequest.of(cursorId,
			size);
		return memberService.searchMemberBookmarkedVotes(memberId, targetId, memberBookmarkVotePageRequest);
	}
}

package com.salmalteam.salmal.presentation.http.vote;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.salmalteam.salmal.auth.annotation.LoginMember;
import com.salmalteam.salmal.vote.application.VoteService;
import com.salmalteam.salmal.vote.dto.request.VoteReportRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/votes")
public class VoteV2Controller {

	private final VoteService voteService;

	@PostMapping("/{vote-id}/reports")
	@ResponseStatus(HttpStatus.CREATED)
	public void reportVote(@LoginMember final Long memberId, @PathVariable(name = "vote-id") final Long voteId,
		VoteReportRequest request) {
		voteService.report(memberId, voteId,request.getReason());
	}
}

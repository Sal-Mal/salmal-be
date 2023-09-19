package com.salmalteam.salmal.presentation.vote;

import com.salmalteam.salmal.application.vote.VoteService;
import com.salmalteam.salmal.domain.vote.evaluation.VoteEvaluationType;
import com.salmalteam.salmal.dto.request.vote.VoteBookmarkRequest;
import com.salmalteam.salmal.dto.request.vote.VoteCommentCreateRequest;
import com.salmalteam.salmal.dto.request.vote.VoteCreateRequest;
import com.salmalteam.salmal.dto.request.vote.VoteEvaluateRequest;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import com.salmalteam.salmal.presentation.Login;
import com.salmalteam.salmal.presentation.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Login
    public void registerVote(@LoginMember final MemberPayLoad memberPayLoad,
                             @ModelAttribute @Valid final VoteCreateRequest voteCreateRequest){
        voteService.register(memberPayLoad, voteCreateRequest);
    }

    @PostMapping("/{vote-id}/evaluations")
    @ResponseStatus(HttpStatus.CREATED)
    @Login
    public void evaluateVote(@LoginMember final MemberPayLoad memberPayLoad,
                             @PathVariable(name = "vote-id") final Long voteId,
                             @RequestBody @Valid final VoteEvaluateRequest voteEvaluateRequest){
        final VoteEvaluationType voteEvaluationType = VoteEvaluationType.from(voteEvaluateRequest.getVoteEvaluationType());
        voteService.evaluate(memberPayLoad, voteId, voteEvaluationType);
    }

    @PostMapping("/{vote-id}/bookmarks")
    @ResponseStatus(HttpStatus.CREATED)
    @Login
    public void bookmarkVote(@LoginMember final MemberPayLoad memberPayLoad,
                             @PathVariable(name = "vote-id") final Long voteId,
                             @RequestBody @Valid final VoteBookmarkRequest voteBookmarkRequest){
        voteService.bookmark(memberPayLoad, voteId, voteBookmarkRequest);
    }

    @PostMapping("/{vote-id}/reports")
    @ResponseStatus(HttpStatus.CREATED)
    @Login
    public void reportVote(@LoginMember final MemberPayLoad memberPayLoad,
                           @PathVariable(name = "vote-id") final Long voteId){
        voteService.report(memberPayLoad, voteId);
    }

    @PostMapping("/{vote-id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @Login
    public void commentVote(@LoginMember final MemberPayLoad memberPayLoad,
                            @PathVariable(name = "vote-id") final Long voteId,
                            @RequestBody @Valid final VoteCommentCreateRequest voteCommentCreateRequest){
        voteService.comment(memberPayLoad, voteId, voteCommentCreateRequest);
    }

}

package com.salmalteam.salmal.presentation.vote;

import com.salmalteam.salmal.application.vote.VoteService;
import com.salmalteam.salmal.domain.vote.evaluation.VoteEvaluationType;
import com.salmalteam.salmal.dto.request.comment.CommentPageRequest;
import com.salmalteam.salmal.dto.request.vote.*;
import com.salmalteam.salmal.dto.response.comment.CommentPageResponse;
import com.salmalteam.salmal.dto.response.vote.VotePageResponse;
import com.salmalteam.salmal.dto.response.vote.VoteResponse;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import com.salmalteam.salmal.presentation.Login;
import com.salmalteam.salmal.presentation.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

    @DeleteMapping("/{vote-id}/evaluations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Login
    public void cancelEvaluation(@LoginMember final MemberPayLoad memberPayLoad,
                                 @PathVariable(name = "vote-id") final Long voteId){
        voteService.cancelEvaluation(memberPayLoad, voteId);
    }

    @PostMapping("/{vote-id}/bookmarks")
    @ResponseStatus(HttpStatus.CREATED)
    @Login
    public void bookmarkVote(@LoginMember final MemberPayLoad memberPayLoad,
                             @PathVariable(name = "vote-id") final Long voteId,
                             @RequestBody @Valid final VoteBookmarkRequest voteBookmarkRequest){
        voteService.bookmark(memberPayLoad, voteId, voteBookmarkRequest);
    }

    @DeleteMapping("/{vote-id}/bookmarks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Login
    public void cancelBookmark(@LoginMember final MemberPayLoad memberPayLoad,
                               @PathVariable(name = "vote-id") final Long voteId){
        voteService.cancelBookmark(memberPayLoad, voteId);
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

    @GetMapping("/{vote-id}/comments")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public CommentPageResponse searchComments(@LoginMember final MemberPayLoad memberPayLoad,
                                              @PathVariable(name = "vote-id") final Long voteId,
                                              @RequestParam(value = "cursor-id", required = false) final Long cursorId,
                                              @RequestParam(value = "size", required = false) final Integer size){
        final CommentPageRequest commentPageRequest = CommentPageRequest.of(cursorId, size);
        return voteService.searchComments(voteId, memberPayLoad, commentPageRequest);
    }

    @GetMapping("/{vote-id}")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public VoteResponse searchVote(@LoginMember final MemberPayLoad memberPayLoad,
                                   @PathVariable(name = "vote-id") final Long voteId){
        return voteService.search(memberPayLoad, voteId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Login
    public VotePageResponse searchVotes(@LoginMember final MemberPayLoad memberPayLoad,
                                        @ModelAttribute final VotePageRequest votePageRequest){

        final SearchTypeConstant searchTypeConstant = SearchTypeConstant.from(votePageRequest.getSearchType());
        return voteService.searchList(memberPayLoad, votePageRequest, searchTypeConstant);
    }

}

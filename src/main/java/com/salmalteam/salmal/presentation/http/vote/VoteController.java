package com.salmalteam.salmal.presentation.http.vote;

import com.salmalteam.salmal.domain.vote.application.VoteService;
import com.salmalteam.salmal.domain.vote.dto.request.VoteCommentCreateRequest;
import com.salmalteam.salmal.domain.vote.dto.request.VoteCreateRequest;
import com.salmalteam.salmal.domain.vote.dto.request.VoteEvaluateRequest;
import com.salmalteam.salmal.domain.vote.dto.request.VotePageRequest;
import com.salmalteam.salmal.domain.vote.entity.evaluation.VoteEvaluationType;
import com.salmalteam.salmal.domain.comment.dto.request.CommentPageRequest;
import com.salmalteam.salmal.domain.comment.dto.response.CommentPageResponse;
import com.salmalteam.salmal.domain.comment.dto.response.CommentResponse;
import com.salmalteam.salmal.domain.vote.dto.response.VotePageResponse;
import com.salmalteam.salmal.domain.vote.dto.response.VoteResponse;
import com.salmalteam.salmal.domain.auth.infrastructure.dto.MemberPayLoad;
import com.salmalteam.salmal.domain.auth.infrastructure.annotation.Login;
import com.salmalteam.salmal.domain.auth.infrastructure.annotation.LoginMember;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
                             @PathVariable(name = "vote-id") final Long voteId){
        voteService.bookmark(memberPayLoad, voteId);
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

    /**
     * iOS 요청 사항 : 댓글 전체 목록 조회 API
     */
    @GetMapping("/{vote-id}/comments/all")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public List<CommentResponse> searchAllComments(@LoginMember final MemberPayLoad memberPayLoad,
                                                   @PathVariable(name = "vote-id") final Long voteId){
        return voteService.searchAllComments(voteId, memberPayLoad);
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

    @DeleteMapping("/{vote-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Login
    public void deleteVote(@LoginMember final MemberPayLoad memberPayLoad,
                           @PathVariable(name = "vote-id") final Long voteId){
        voteService.delete(memberPayLoad, voteId);
    }

}

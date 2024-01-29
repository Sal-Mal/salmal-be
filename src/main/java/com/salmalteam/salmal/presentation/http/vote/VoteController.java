package com.salmalteam.salmal.presentation.http.vote;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.salmalteam.salmal.auth.annotation.Login;
import com.salmalteam.salmal.auth.annotation.LoginMember;
import com.salmalteam.salmal.auth.entity.AuthPayload;
import com.salmalteam.salmal.comment.dto.request.CommentPageRequest;
import com.salmalteam.salmal.comment.dto.response.CommentPageResponse;
import com.salmalteam.salmal.comment.dto.response.CommentResponse;
import com.salmalteam.salmal.vote.application.VoteService;
import com.salmalteam.salmal.vote.dto.request.VoteCommentCreateRequest;
import com.salmalteam.salmal.vote.dto.request.VoteCreateRequest;
import com.salmalteam.salmal.vote.dto.request.VoteEvaluateRequest;
import com.salmalteam.salmal.vote.dto.request.VotePageRequest;
import com.salmalteam.salmal.vote.dto.response.VotePageResponse;
import com.salmalteam.salmal.vote.dto.response.VoteResponse;
import com.salmalteam.salmal.vote.entity.evaluation.VoteEvaluationType;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Login
    public void registerVote(@LoginMember final AuthPayload authPayload,
                             @ModelAttribute @Valid final VoteCreateRequest voteCreateRequest){
        voteService.register(authPayload, voteCreateRequest);
    }

    @PostMapping("/{vote-id}/evaluations")
    @ResponseStatus(HttpStatus.CREATED)
    @Login
    public void evaluateVote(@LoginMember final AuthPayload authPayload,
                             @PathVariable(name = "vote-id") final Long voteId,
                             @RequestBody @Valid final VoteEvaluateRequest voteEvaluateRequest){
        final VoteEvaluationType voteEvaluationType = VoteEvaluationType.from(voteEvaluateRequest.getVoteEvaluationType());
        voteService.evaluate(authPayload, voteId, voteEvaluationType);
    }

    @DeleteMapping("/{vote-id}/evaluations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Login
    public void cancelEvaluation(@LoginMember final AuthPayload authPayload,
                                 @PathVariable(name = "vote-id") final Long voteId){
        voteService.cancelEvaluation(authPayload, voteId);
    }

    @PostMapping("/{vote-id}/bookmarks")
    @ResponseStatus(HttpStatus.CREATED)
    @Login
    public void bookmarkVote(@LoginMember final AuthPayload authPayload,
                             @PathVariable(name = "vote-id") final Long voteId){
        voteService.bookmark(authPayload, voteId);
    }

    @DeleteMapping("/{vote-id}/bookmarks")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Login
    public void cancelBookmark(@LoginMember final AuthPayload authPayload,
                               @PathVariable(name = "vote-id") final Long voteId){
        voteService.cancelBookmark(authPayload, voteId);
    }

    @PostMapping("/{vote-id}/reports")
    @ResponseStatus(HttpStatus.CREATED)
    @Login
    public void reportVote(@LoginMember final AuthPayload authPayload,
                           @PathVariable(name = "vote-id") final Long voteId){
        voteService.report(authPayload, voteId);
    }

    @PostMapping("/{vote-id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @Login
    public void commentVote(@LoginMember final AuthPayload authPayload,
                            @PathVariable(name = "vote-id") final Long voteId,
                            @RequestBody @Valid final VoteCommentCreateRequest voteCommentCreateRequest){
        voteService.comment(authPayload, voteId, voteCommentCreateRequest);
    }

    @GetMapping("/{vote-id}/comments")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public CommentPageResponse searchComments(@LoginMember final AuthPayload authPayload,
                                              @PathVariable(name = "vote-id") final Long voteId,
                                              @RequestParam(value = "cursor-id", required = false) final Long cursorId,
                                              @RequestParam(value = "size", required = false) final Integer size){
        final CommentPageRequest commentPageRequest = CommentPageRequest.of(cursorId, size);
        return voteService.searchComments(voteId, authPayload, commentPageRequest);
    }

    /**
     * iOS 요청 사항 : 댓글 전체 목록 조회 API
     */
    @GetMapping("/{vote-id}/comments/all")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public List<CommentResponse> searchAllComments(@LoginMember final AuthPayload authPayload,
                                                   @PathVariable(name = "vote-id") final Long voteId){
        return voteService.searchAllComments(voteId, authPayload);
    }

    @GetMapping("/{vote-id}")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public VoteResponse searchVote(@LoginMember final AuthPayload authPayload,
                                   @PathVariable(name = "vote-id") final Long voteId){
        return voteService.search(authPayload, voteId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Login
    public VotePageResponse searchVotes(@LoginMember final AuthPayload authPayload,
                                        @ModelAttribute final VotePageRequest votePageRequest){

        final SearchTypeConstant searchTypeConstant = SearchTypeConstant.from(votePageRequest.getSearchType());
        return voteService.searchList(authPayload, votePageRequest, searchTypeConstant);
    }

    @DeleteMapping("/{vote-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Login
    public void deleteVote(@LoginMember final AuthPayload authPayload,
                           @PathVariable(name = "vote-id") final Long voteId){
        voteService.delete(authPayload, voteId);
    }

}

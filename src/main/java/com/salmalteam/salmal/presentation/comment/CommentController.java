package com.salmalteam.salmal.presentation.comment;

import com.salmalteam.salmal.application.comment.CommentService;
import com.salmalteam.salmal.dto.request.comment.CommentPageRequest;
import com.salmalteam.salmal.dto.request.vote.VoteCommentUpdateRequest;
import com.salmalteam.salmal.dto.response.comment.CommentPageResponse;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import com.salmalteam.salmal.presentation.Login;
import com.salmalteam.salmal.presentation.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    @PutMapping("/{comment-id}")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public void updateComment(@LoginMember final MemberPayLoad memberPayLoad,
                              @PathVariable(name = "comment-id") final Long commentId,
                              @RequestBody @Valid final VoteCommentUpdateRequest voteCommentUpdateRequest){
        commentService.updateComment(memberPayLoad, commentId, voteCommentUpdateRequest);
    }
    @PostMapping("/{comment-id}/likes")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public void likeComment(@LoginMember final MemberPayLoad memberPayLoad,
                            @PathVariable(name = "comment-id") final Long commentId){
        commentService.likeComment(memberPayLoad, commentId);
    }

    @DeleteMapping("/{comment-id}/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Login
    public void cancelLikeComment(@LoginMember final MemberPayLoad memberPayLoad,
                                  @PathVariable(name = "comment-id") final Long commentId){
        commentService.unLikeComment(memberPayLoad, commentId);
    }

    @PostMapping("/{comment-id}/reports")
    @ResponseStatus(HttpStatus.CREATED)
    @Login
    public void reportComment(@LoginMember final MemberPayLoad memberPayLoad,
                              @PathVariable(name = "comment-id") final Long commentId){
        commentService.report(memberPayLoad, commentId);
    }


}

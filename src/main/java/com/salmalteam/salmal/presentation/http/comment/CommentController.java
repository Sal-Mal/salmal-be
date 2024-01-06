package com.salmalteam.salmal.presentation.http.comment;

import com.salmalteam.salmal.domain.comment.application.CommentService;
import com.salmalteam.salmal.domain.comment.dto.request.CommentReplyCreateRequest;
import com.salmalteam.salmal.domain.comment.dto.request.ReplyPageRequest;
import com.salmalteam.salmal.domain.vote.dto.request.VoteCommentUpdateRequest;
import com.salmalteam.salmal.domain.comment.dto.response.ReplyPageResponse;
import com.salmalteam.salmal.domain.comment.dto.response.ReplyResponse;
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

    @DeleteMapping("/{comment-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Login
    public void deleteComment(@LoginMember final MemberPayLoad memberPayLoad,
                              @PathVariable(name = "comment-id") final Long commentId){
        commentService.deleteComment(memberPayLoad, commentId);
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

    @PostMapping("/{comment-id}/replies")
    @ResponseStatus(HttpStatus.CREATED)
    @Login
    public void replyComment(@LoginMember final MemberPayLoad memberPayLoad,
                             @PathVariable(name = "comment-id") final Long commentId,
                             @RequestBody @Valid CommentReplyCreateRequest commentReplyCreateRequest){
        commentService.replyComment(memberPayLoad, commentId, commentReplyCreateRequest);
    }

    @GetMapping("/{comment-id}/replies")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public ReplyPageResponse searchReplies(@LoginMember final MemberPayLoad memberPayLoad,
                                           @PathVariable(name = "comment-id") final Long commentId,
                                           @RequestParam(value = "cursor-id", required = false) final Long cursorId,
                                           @RequestParam(value = "size", required = false) final Integer size){
        final ReplyPageRequest replyPageRequest = ReplyPageRequest.of(cursorId, size);
        return commentService.searchReplies(memberPayLoad, commentId, replyPageRequest);
    }

    @GetMapping("/{comment-id}/replies/all")
    @ResponseStatus(HttpStatus.OK)
    @Login
    public List<ReplyResponse> searchAllReplies(@LoginMember final MemberPayLoad memberPayLoad,
                                                @PathVariable(name = "comment-id") final Long commentId){

        return commentService.searchAllReplies(memberPayLoad, commentId);
    }


}

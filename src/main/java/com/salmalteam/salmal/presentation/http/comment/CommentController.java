package com.salmalteam.salmal.presentation.http.comment;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.salmalteam.salmal.auth.annotation.LoginMember;
import com.salmalteam.salmal.auth.entity.AuthPayload;
import com.salmalteam.salmal.comment.application.CommentService;
import com.salmalteam.salmal.comment.dto.request.CommentReplyCreateRequest;
import com.salmalteam.salmal.comment.dto.request.ReplyPageRequest;
import com.salmalteam.salmal.comment.dto.response.ReplayCommentDto;
import com.salmalteam.salmal.comment.dto.response.ReplyPageResponse;
import com.salmalteam.salmal.comment.dto.response.ReplyResponse;
import com.salmalteam.salmal.notification.dto.MessageSpec;
import com.salmalteam.salmal.notification.service.NotificationPublisher;
import com.salmalteam.salmal.notification.service.NotificationService;
import com.salmalteam.salmal.vote.dto.request.VoteCommentUpdateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final NotificationService notificationService;
    private final NotificationPublisher notificationPublisher;

    @PutMapping("/{comment-id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateComment(@LoginMember final Long memberId,
                              @PathVariable(name = "comment-id") final Long commentId,
                              @RequestBody @Valid final VoteCommentUpdateRequest voteCommentUpdateRequest){
        commentService.updateComment(memberId, commentId, voteCommentUpdateRequest);
    }

    @DeleteMapping("/{comment-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@LoginMember final Long memberId,
                              @PathVariable(name = "comment-id") final Long commentId){
        commentService.deleteComment(memberId, commentId);
    }

    @PostMapping("/{comment-id}/likes")
    @ResponseStatus(HttpStatus.OK)
    public void likeComment(@LoginMember final Long memberId,
                            @PathVariable(name = "comment-id") final Long commentId){
        commentService.likeComment(memberId, commentId);
    }

    @DeleteMapping("/{comment-id}/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelLikeComment(@LoginMember final Long memberId,
                                  @PathVariable(name = "comment-id") final Long commentId){
        commentService.unLikeComment(memberId, commentId);
    }

    @PostMapping("/{comment-id}/reports")
    @ResponseStatus(HttpStatus.CREATED)
    public void reportComment(@LoginMember final Long memberId,
                              @PathVariable(name = "comment-id") final Long commentId){
        commentService.report(memberId, commentId);
    }

    @PostMapping("/{comment-id}/replies")
    @ResponseStatus(HttpStatus.CREATED)
    public void replyComment(@LoginMember final Long memberId,
        @PathVariable(name = "comment-id") final Long commentId,
        @RequestBody @Valid CommentReplyCreateRequest request) {
        ReplayCommentDto replayComment = commentService.replyComment(memberId, commentId, request);
        if (replayComment.isSameCommenter()) {
            return;
        }
        MessageSpec messageSpec = notificationService.save(
            replayComment.getCommentOwnerId(), replayComment.getCommentId(),
            replayComment.getNickName(), replayComment.getContent(), replayComment.getVoteId(),
            replayComment.getReplyerImageUrl(), replayComment.getVoteImageUrl());
        notificationPublisher.pub(messageSpec);
    }

    @GetMapping("/{comment-id}/replies")
    @ResponseStatus(HttpStatus.OK)
    public ReplyPageResponse searchReplies(@LoginMember final Long memberId,
                                           @PathVariable(name = "comment-id") final Long commentId,
                                           @RequestParam(value = "cursor-id", required = false) final Long cursorId,
                                           @RequestParam(value = "size", required = false) final Integer size){
        final ReplyPageRequest replyPageRequest = ReplyPageRequest.of(cursorId, size);
        return commentService.searchReplies(memberId, commentId, replyPageRequest);
    }

    @GetMapping("/{comment-id}/replies/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ReplyResponse> searchAllReplies(@LoginMember final Long memberId,
                                                @PathVariable(name = "comment-id") final Long commentId){

        return commentService.searchAllReplies(memberId, commentId);
    }
}

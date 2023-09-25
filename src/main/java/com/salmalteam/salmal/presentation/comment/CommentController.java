package com.salmalteam.salmal.presentation.comment;

import com.salmalteam.salmal.application.comment.CommentService;
import com.salmalteam.salmal.dto.request.vote.VoteCommentUpdateRequest;
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
}
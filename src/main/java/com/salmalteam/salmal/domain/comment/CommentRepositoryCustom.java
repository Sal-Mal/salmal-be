package com.salmalteam.salmal.domain.comment;

import com.salmalteam.salmal.dto.request.comment.CommentPageRequest;
import com.salmalteam.salmal.dto.request.comment.ReplyPageRequest;
import com.salmalteam.salmal.dto.response.comment.CommentPageResponse;
import com.salmalteam.salmal.dto.response.comment.CommentResponse;
import com.salmalteam.salmal.dto.response.comment.ReplyPageResponse;
import com.salmalteam.salmal.dto.response.comment.ReplyResponse;

import java.util.List;

public interface CommentRepositoryCustom {
    CommentPageResponse searchList(final Long voteId, final Long memberId, final CommentPageRequest commentPageRequest);
    List<CommentResponse> searchAllList(final Long voteId, final Long memberId);
    ReplyPageResponse searchReplies(final Long parentCommentId, final Long memberId, final ReplyPageRequest replyPageRequest);
    List<ReplyResponse> searchAllReplies(final Long parentCommentId, final Long memberId);
}

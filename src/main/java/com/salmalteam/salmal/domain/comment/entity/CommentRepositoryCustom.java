package com.salmalteam.salmal.domain.comment.entity;

import com.salmalteam.salmal.domain.comment.dto.request.CommentPageRequest;
import com.salmalteam.salmal.domain.comment.dto.request.ReplyPageRequest;
import com.salmalteam.salmal.domain.comment.dto.response.CommentPageResponse;
import com.salmalteam.salmal.domain.comment.dto.response.CommentResponse;
import com.salmalteam.salmal.domain.comment.dto.response.ReplyPageResponse;
import com.salmalteam.salmal.domain.comment.dto.response.ReplyResponse;

import java.util.List;

public interface CommentRepositoryCustom {
    CommentPageResponse searchList(final Long voteId, final Long memberId, final CommentPageRequest commentPageRequest);
    List<CommentResponse> searchAllList(final Long voteId, final Long memberId);
    ReplyPageResponse searchReplies(final Long parentCommentId, final Long memberId, final ReplyPageRequest replyPageRequest);
    List<ReplyResponse> searchAllReplies(final Long parentCommentId, final Long memberId);
}

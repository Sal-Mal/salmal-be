package com.salmalteam.salmal.domain.comment;

import com.salmalteam.salmal.dto.request.comment.CommentPageRequest;
import com.salmalteam.salmal.dto.response.comment.CommentPageResponse;
import com.salmalteam.salmal.dto.response.comment.CommentResponse;

import java.util.List;

public interface CommentRepositoryCustom {
    CommentPageResponse searchList(final Long voteId, final Long memberId, final CommentPageRequest commentPageRequest);
    List<CommentResponse> searchAllList(final Long voteId, final Long memberId);
}

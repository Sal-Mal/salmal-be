package com.salmalteam.salmal.domain.comment;

import com.salmalteam.salmal.dto.request.comment.CommentPageRequest;
import com.salmalteam.salmal.dto.response.comment.CommentPageResponse;

public interface CommentRepositoryCustom {
    CommentPageResponse searchList(final Long voteId, final Long memberId, final CommentPageRequest commentPageRequest);
}

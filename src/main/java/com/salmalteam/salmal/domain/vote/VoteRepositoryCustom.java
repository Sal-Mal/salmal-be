package com.salmalteam.salmal.domain.vote;

import com.salmalteam.salmal.dto.request.vote.VotePageRequest;
import com.salmalteam.salmal.dto.response.vote.VotePageResponse;
import com.salmalteam.salmal.dto.response.vote.VoteResponse;
import com.salmalteam.salmal.presentation.vote.SearchTypeConstant;

public interface VoteRepositoryCustom {

    VoteResponse search(final Long id, final Long memberId);
    VotePageResponse searchList(final Long memberId, final VotePageRequest votePageRequest, final SearchTypeConstant searchTypeConstant);
}

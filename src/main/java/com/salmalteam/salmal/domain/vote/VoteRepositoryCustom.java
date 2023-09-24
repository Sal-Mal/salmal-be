package com.salmalteam.salmal.domain.vote;

import com.salmalteam.salmal.dto.response.vote.VoteResponse;

public interface VoteRepositoryCustom {

    VoteResponse search(final Long id, final Long memberId);
}

package com.salmalteam.salmal.domain.vote;

import com.salmalteam.salmal.dto.request.member.vote.MemberBookmarkVotePageRequest;
import com.salmalteam.salmal.dto.request.member.vote.MemberEvaluationVotePageRequest;
import com.salmalteam.salmal.dto.request.member.vote.MemberVotePageRequest;
import com.salmalteam.salmal.dto.request.vote.VotePageRequest;
import com.salmalteam.salmal.dto.response.member.vote.MemberBookmarkVotePageResponse;
import com.salmalteam.salmal.dto.response.member.vote.MemberEvaluationVotePageResponse;
import com.salmalteam.salmal.dto.response.member.vote.MemberVotePageResponse;
import com.salmalteam.salmal.dto.response.vote.VotePageResponse;
import com.salmalteam.salmal.dto.response.vote.VoteResponse;
import com.salmalteam.salmal.presentation.vote.SearchTypeConstant;

public interface VoteRepositoryCustom {

    VoteResponse search(final Long id, final Long memberId);
    VotePageResponse searchList(final Long memberId, final VotePageRequest votePageRequest, final SearchTypeConstant searchTypeConstant);
    MemberVotePageResponse searchMemberVoteList(final Long memberId, final MemberVotePageRequest memberVotePageRequest);
    MemberEvaluationVotePageResponse searchMemberEvaluationVoteList(final Long memberId, final MemberEvaluationVotePageRequest memberEvaluationVotePageRequest);

    MemberBookmarkVotePageResponse searchMemberBookmarkVoteList(final Long memberId, final MemberBookmarkVotePageRequest memberBookmarkVotePageRequest);
}

package com.salmalteam.salmal.vote.entity;

import com.salmalteam.salmal.member.dto.request.vote.MemberBookmarkVotePageRequest;
import com.salmalteam.salmal.member.dto.request.vote.MemberEvaluationVotePageRequest;
import com.salmalteam.salmal.member.dto.request.vote.MemberVotePageRequest;
import com.salmalteam.salmal.vote.dto.request.VotePageRequest;
import com.salmalteam.salmal.member.dto.response.vote.MemberBookmarkVotePageResponse;
import com.salmalteam.salmal.member.dto.response.vote.MemberEvaluationVotePageResponse;
import com.salmalteam.salmal.member.dto.response.vote.MemberVotePageResponse;
import com.salmalteam.salmal.vote.dto.response.VotePageResponse;
import com.salmalteam.salmal.vote.dto.response.VoteResponse;
import com.salmalteam.salmal.presentation.http.vote.SearchTypeConstant;

public interface VoteRepositoryCustom {

    VoteResponse search(final Long id, final Long memberId);
    VotePageResponse searchList(final Long memberId, final VotePageRequest votePageRequest, final SearchTypeConstant searchTypeConstant);
    MemberVotePageResponse searchMemberVoteList(final Long memberId, final MemberVotePageRequest memberVotePageRequest);
    MemberEvaluationVotePageResponse searchMemberEvaluationVoteList(final Long memberId, final MemberEvaluationVotePageRequest memberEvaluationVotePageRequest);
    MemberBookmarkVotePageResponse searchMemberBookmarkVoteList(final Long memberId, final MemberBookmarkVotePageRequest memberBookmarkVotePageRequest);
}

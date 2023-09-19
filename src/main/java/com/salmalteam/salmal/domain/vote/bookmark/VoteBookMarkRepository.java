package com.salmalteam.salmal.domain.vote.bookmark;

import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.vote.Vote;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface VoteBookMarkRepository extends Repository<VoteBookMark, Long> {
    VoteBookMark save(VoteBookMark voteBookMark);
    Optional<VoteBookMark> findByVoteAndBookmaker(Vote vote, Member member);
}

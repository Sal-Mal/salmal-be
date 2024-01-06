package com.salmalteam.salmal.domain.vote.entity;

import com.salmalteam.salmal.domain.member.entity.Member;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface VoteBookMarkRepository extends Repository<VoteBookMark, Long> {
    VoteBookMark save(VoteBookMark voteBookMark);
    Optional<VoteBookMark> findByVoteAndBookmaker(Vote vote, Member member);
    boolean existsByVoteAndBookmaker(Vote vote, Member member);
    void deleteByVoteAndBookmaker(Vote vote, Member member);
}

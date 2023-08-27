package com.salmalteam.salmal.domain.vote.report;

import com.salmalteam.salmal.domain.BaseCreatedTimeEntity;
import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.vote.Vote;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteReport extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private Member reporter;

    private VoteReport(final Vote vote, final Member member){
        this.vote = vote;
        this.reporter = member;
    }
    public static VoteReport of(final Vote vote, final Member member){
        return new VoteReport(vote, member);
    }
}

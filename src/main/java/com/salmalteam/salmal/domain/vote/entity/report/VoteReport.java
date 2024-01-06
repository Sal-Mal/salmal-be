package com.salmalteam.salmal.domain.vote.entity.report;

import com.salmalteam.salmal.common.entity.BaseCreatedTimeEntity;
import com.salmalteam.salmal.domain.member.entity.Member;
import com.salmalteam.salmal.domain.vote.entity.Vote;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@Table(name = "vote_report")
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

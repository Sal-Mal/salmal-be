package com.salmalteam.salmal.domain.member.evaluation;

import com.salmalteam.salmal.domain.BaseCreatedTimeEntity;
import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.vote.Vote;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
@Table(name = "evaluation_vote")
public class EvaluationVote extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluator_id")
    private Member evaluator;

    private EvaluationVote(final Vote vote, final Member member) {
        this.vote = vote;
        this.evaluator = member;
    }

    public static EvaluationVote of(final Vote vote, final Member member) {
        return new EvaluationVote(vote, member);
    }
}

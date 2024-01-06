package com.salmalteam.salmal.vote.entity.evaluation;

import com.salmalteam.salmal.vote.entity.Vote;
import com.salmalteam.salmal.common.entity.BaseCreatedTimeEntity;
import com.salmalteam.salmal.member.entity.Member;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@Table(name = "vote_evaluation")
public class VoteEvaluation extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluator_id")
    private Member evaluator;

    @Enumerated(EnumType.STRING)
    private VoteEvaluationType voteEvaluationType;

    private VoteEvaluation(final Vote vote, final Member member, final VoteEvaluationType voteEvaluationType) {
        this.vote = vote;
        this.evaluator = member;
        this.voteEvaluationType = voteEvaluationType;
    }

    public static VoteEvaluation of(final Vote vote, final Member member, final VoteEvaluationType voteEvaluationType) {
        return new VoteEvaluation(vote, member, voteEvaluationType);
    }
}

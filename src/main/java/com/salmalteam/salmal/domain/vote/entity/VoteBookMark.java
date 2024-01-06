package com.salmalteam.salmal.domain.vote.entity;

import com.salmalteam.salmal.infra.jpa.BaseCreatedTimeEntity;
import com.salmalteam.salmal.domain.member.entity.Member;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@Table(name = "vote_bookmark")
public class VoteBookMark extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmarker_id")
    private Member bookmaker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    private VoteBookMark(final Member member, final Vote vote) {
        this.vote = vote;
        this.bookmaker = member;
    }

    public static VoteBookMark of(final Member member, final Vote vote) {
        return new VoteBookMark(member, vote);
    }
}

package com.salmalteam.salmal.domain.vote;

import com.salmalteam.salmal.domain.BaseCreatedTimeEntity;
import com.salmalteam.salmal.domain.member.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "vote_member")
public class VoteMember extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id")
    private Member voter;

    private VoteMember(final Vote vote, final Member member) {
        this.vote = vote;
        this.voter = member;
    }

    public static VoteMember of(final Vote vote, final Member member) {
        return new VoteMember(vote, member);
    }


}

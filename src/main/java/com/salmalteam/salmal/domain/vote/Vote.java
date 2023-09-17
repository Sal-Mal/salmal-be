package com.salmalteam.salmal.domain.vote;

import com.salmalteam.salmal.domain.member.Member;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private VoteImage voteImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private int viewCount;

    private Vote(final String imageUrl, final Member member){
        this.voteImage = VoteImage.of(imageUrl);
        this.member = member;
    }

    public static Vote of(final String imageUrl, final Member member){
        return new Vote(imageUrl, member);
    }
}

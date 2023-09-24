package com.salmalteam.salmal.domain.vote;

import com.salmalteam.salmal.domain.BaseCreatedTimeEntity;
import com.salmalteam.salmal.domain.member.Member;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Entity
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private VoteImage voteImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private int viewCount = 0;

    @Column
    private int commentCount = 0;

    @Column
    private int evaluationCount = 0;

    @Column
    private int likeCount = 0;

    @Column
    private int dislikeCount = 0;

    @Column
    private BigDecimal likeRatio = BigDecimal.ZERO;

    @Column
    private BigDecimal dislikeRatio = BigDecimal.ZERO;

    private Vote(final String imageUrl, final Member member){
        this.voteImage = VoteImage.of(imageUrl);
        this.member = member;
    }

    public static Vote of(final String imageUrl, final Member member){
        return new Vote(imageUrl, member);
    }
}

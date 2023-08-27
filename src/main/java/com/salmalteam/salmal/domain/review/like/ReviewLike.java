package com.salmalteam.salmal.domain.review.like;

import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.review.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liker_id")
    private Member liker;

    private ReviewLike(final Review review, final Member member){
        this.review = review;
        this.liker = member;
    }

    public static ReviewLike of(final Review review, final Member member){
        return new ReviewLike(review, member);
    }

}

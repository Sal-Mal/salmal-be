package com.salmalteam.salmal.domain.review.report;

import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.review.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private Member reporter;

    private ReviewReport(final Review review, final Member member){
        this.review = review;
        this.reporter = member;
    }
    public static ReviewReport of(final Review review, final Member member){
        return new ReviewReport(review, member);
    }

}

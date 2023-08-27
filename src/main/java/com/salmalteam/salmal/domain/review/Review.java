package com.salmalteam.salmal.domain.review;

import com.salmalteam.salmal.domain.BaseEntity;
import com.salmalteam.salmal.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private Content content;

    private Review(final Content content, final Member member){
        this.content = content;
        this.member = member;
    }

    public static Review of(final Content content, final Member member){
        return new Review(content, member);
    }
}

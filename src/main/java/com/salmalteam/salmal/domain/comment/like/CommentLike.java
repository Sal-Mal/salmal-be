package com.salmalteam.salmal.domain.comment.like;

import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.comment.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liker_id")
    private Member liker;

    private CommentLike(final Comment comment, final Member member){
        this.comment = comment;
        this.liker = member;
    }

    public static CommentLike of(final Comment comment, final Member member){
        return new CommentLike(comment, member);
    }

}

package com.salmalteam.salmal.domain.comment.entity.report;

import com.salmalteam.salmal.domain.member.entity.Member;
import com.salmalteam.salmal.domain.comment.entity.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private Member reporter;

    private CommentReport(final Comment comment, final Member member){
        this.comment = comment;
        this.reporter = member;
    }
    public static CommentReport of(final Comment comment, final Member member){
        return new CommentReport(comment, member);
    }

}

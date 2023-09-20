package com.salmalteam.salmal.domain.vote.comment;

import com.salmalteam.salmal.domain.BaseEntity;
import com.salmalteam.salmal.domain.member.Member;
import com.salmalteam.salmal.domain.vote.Vote;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commenter_id")
    private Member commenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @Embedded
    private Content content;

    private Comment(final String content, final Vote vote, final Member commenter){
        this.content = Content.of(content);
        this.commenter = commenter;
        this.vote = vote;
    }

    public static Comment of(final String content, final Vote vote, final Member commenter){
        return new Comment(content, vote, commenter);
    }

    public void updateContent(final String content){
        this.content = Content.of(content);
    }
}

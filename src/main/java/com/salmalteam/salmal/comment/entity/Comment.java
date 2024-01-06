package com.salmalteam.salmal.comment.entity;

import com.salmalteam.salmal.common.entity.BaseEntity;
import com.salmalteam.salmal.comment.entity.like.CommentLike;
import com.salmalteam.salmal.comment.entity.report.CommentReport;
import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.vote.entity.Vote;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<CommentReport> commentReports = new ArrayList<>();

    @Embedded
    private Content content;

    @Column
    private int likeCount = 0;

    @Column
    private int replyCount = 0;

    @Column
    @Enumerated(EnumType.STRING)
    private CommentType commentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> childComments = new ArrayList<>();

    public void decreaseReplyCount(final int size){
        this.replyCount -= size;
    }
    public void decreaseLikeCount(final int size){
        this.likeCount -= size;
    }

    private Comment(final String content, final Vote vote, final Member commenter){
        this.content = Content.of(content);
        this.commenter = commenter;
        this.vote = vote;
        this.commentType = CommentType.COMMENT;
    }

    private Comment(final String content, final Comment parentComment, final Member replier){
        this.content = Content.of(content);
        this.commenter = replier;
        this.parentComment = parentComment;
        this.commentType =CommentType.REPLY;
    }

    public static Comment of(final String content, final Vote vote, final Member commenter){
        return new Comment(content, vote, commenter);
    }

    public static Comment ofReply(final String content, final Comment parentComment, final Member replier){
        return new Comment(content, parentComment, replier);
    }

    public void updateContent(final String content){
        this.content = Content.of(content);
    }
}

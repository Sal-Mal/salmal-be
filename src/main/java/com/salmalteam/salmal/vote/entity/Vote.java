package com.salmalteam.salmal.vote.entity;

import com.salmalteam.salmal.vote.entity.report.VoteReport;
import com.salmalteam.salmal.common.entity.BaseCreatedTimeEntity;
import com.salmalteam.salmal.comment.entity.Comment;
import com.salmalteam.salmal.member.entity.Member;
import com.salmalteam.salmal.vote.entity.evaluation.VoteEvaluation;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private VoteImage voteImage;

    @OneToMany(mappedBy = "vote", cascade = CascadeType.REMOVE)
    private List<VoteBookMark> voteBookMarks = new ArrayList<>();

    @OneToMany(mappedBy = "vote", cascade = CascadeType.REMOVE)
    private List<VoteEvaluation> voteEvaluations = new ArrayList<>();

    @OneToMany(mappedBy = "vote", cascade = CascadeType.REMOVE)
    private List<VoteReport> voteReports = new ArrayList<>();

    @OneToMany(mappedBy = "vote", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

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

    public void decreaseCommentCount(final int size){
        this.commentCount -= size;
    }

    public static Vote of(final String imageUrl, final Member member){
        return new Vote(imageUrl, member);
    }
}

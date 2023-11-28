package com.salmalteam.salmal.domain.member;

import com.salmalteam.salmal.domain.BaseEntity;
import com.salmalteam.salmal.domain.comment.Comment;
import com.salmalteam.salmal.domain.comment.like.CommentLike;
import com.salmalteam.salmal.domain.comment.report.CommentReport;
import com.salmalteam.salmal.domain.vote.Vote;
import com.salmalteam.salmal.domain.vote.bookmark.VoteBookMark;
import com.salmalteam.salmal.domain.vote.evaluation.VoteEvaluation;
import com.salmalteam.salmal.domain.vote.report.VoteReport;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @OneToMany(mappedBy = "commenter", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "reporter", cascade = CascadeType.REMOVE)
    private List<CommentReport> commentReports = new ArrayList<>();

    @OneToMany(mappedBy = "liker", cascade = CascadeType.REMOVE)
    private List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Vote> votes = new ArrayList<>();

    @OneToMany(mappedBy = "reporter", cascade = CascadeType.REMOVE)
    private List<VoteReport> voteReports = new ArrayList<>();

    @OneToMany(mappedBy = "bookmaker", cascade = CascadeType.REMOVE)
    private List<VoteBookMark> voteBookMarks = new ArrayList<>();

    @OneToMany(mappedBy = "evaluator", cascade = CascadeType.REMOVE)
    private List<VoteEvaluation> voteEvaluations = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Embedded
    private NickName nickName;

    @Embedded
    private Introduction introduction;

    @Embedded
    private Setting setting;

    @Embedded
    private MemberImage memberImage;

    @Builder(access = AccessLevel.PRIVATE)
    private Member(final String providerId, final String nickName, final String provider, final Boolean marketingInformationConsent){
        this.providerId = providerId;
        this.nickName = NickName.from(nickName);
        this.memberImage = MemberImage.initMemberImage();
        this.introduction = Introduction.initIntroduction();
        this.provider = Provider.from(provider);
        this.setting = Setting.of(marketingInformationConsent);
    }
    public static Member of(final String providerId, final String nickName, final String provider, final Boolean marketingInformationConsent){
        return Member.builder()
                .providerId(providerId)
                .nickName(nickName)
                .provider(provider)
                .marketingInformationConsent(marketingInformationConsent)
                .build();
    }

    public void updateMyPage(final String nickName, final String introduction){
        this.nickName = NickName.from(nickName);
        this.introduction = Introduction.from(introduction);
    }

    public void updateImage(final String imageUrl){
        this.memberImage = MemberImage.of(imageUrl);
    }

}

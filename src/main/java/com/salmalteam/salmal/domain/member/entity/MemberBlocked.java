package com.salmalteam.salmal.domain.member.entity;

import com.salmalteam.salmal.common.entity.BaseCreatedTimeEntity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@Table(name = "member_blocked")
public class MemberBlocked extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id")
    private Member blocker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target__id")
    private Member target;

    private MemberBlocked(final Member blocker, final Member target) {
        this.blocker = blocker;
        this.target = target;
    }

    public static MemberBlocked of(final Member blocker, final Member blockedMember) {
        return new MemberBlocked(blocker, blockedMember);
    }
}

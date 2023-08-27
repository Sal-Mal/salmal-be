package com.salmalteam.salmal.domain.member.block;

import com.salmalteam.salmal.domain.BaseCreatedTimeEntity;
import com.salmalteam.salmal.domain.member.Member;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
@Table(name = "blocked_member")
public class BlockedMember extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id")
    private Member blocker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked__id")
    private Member blockedMember;

    private BlockedMember(final Member blocker, final Member blockedMember) {
        this.blocker = blocker;
        this.blockedMember = blockedMember;
    }

    public static BlockedMember of(final Member blocker, final Member blockedMember) {
        return new BlockedMember(blocker, blockedMember);
    }
}

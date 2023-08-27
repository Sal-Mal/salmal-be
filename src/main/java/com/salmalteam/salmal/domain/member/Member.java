package com.salmalteam.salmal.domain.member;

import com.salmalteam.salmal.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private NickName nickName;

    @Embedded
    private MemberImage memberImage;

    private Member(final NickName nickName, final MemberImage memberImage){
        this.nickName = nickName;
        this.memberImage = memberImage;
    }

    public static Member of(final NickName nickName, final MemberImage memberImage){
        return new Member(nickName, memberImage);
    }

}

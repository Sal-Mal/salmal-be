package com.salmalteam.salmal.domain.member;

import com.salmalteam.salmal.domain.BaseEntity;
import lombok.*;

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

    @Column(name = "provider_id", nullable = false)
    private String providerId;

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

}

package com.salmalteam.salmal.domain.member;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.salmalteam.salmal.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@Enumerated(EnumType.STRING)
	private Status status;

	@Builder(access = AccessLevel.PRIVATE)
	private Member(final String providerId, final String nickName, final String provider,
		final Boolean marketingInformationConsent, Status status) {
		this.providerId = providerId;
		this.nickName = NickName.from(nickName);
		this.memberImage = MemberImage.initMemberImage();
		this.introduction = Introduction.initIntroduction();
		this.provider = Provider.from(provider);
		this.setting = Setting.of(marketingInformationConsent);
		this.status = status;
	}

	public static Member createActivatedMember(final String providerId, final String nickName, final String provider,
		final Boolean marketingInformationConsent) {
		return Member.builder()
			.providerId(providerId)
			.nickName(nickName)
			.provider(provider)
			.marketingInformationConsent(marketingInformationConsent)
			.status(Status.ACTIVATED)
			.build();
	}

	public void updateMyPage(final String nickName, final String introduction) {
		this.nickName = NickName.from(nickName);
		this.introduction = Introduction.from(introduction);
	}

	public void updateImage(final String imageUrl) {
		this.memberImage = MemberImage.of(imageUrl);
	}

	public boolean isRemoved() {
		return status.equals(Status.REMOVED);
	}

	public void remove() {
		status = Status.REMOVED;
	}
}

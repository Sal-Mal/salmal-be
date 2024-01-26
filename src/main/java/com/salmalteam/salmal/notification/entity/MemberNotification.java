package com.salmalteam.salmal.notification.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberNotification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long memberId;
	private String notificationToken;

	public static MemberNotification create(Long memberId, String notificationToken) {
		return new MemberNotification(null, memberId, notificationToken);
	}

	public void updateToken(String token) {
		notificationToken = token;
	}
}
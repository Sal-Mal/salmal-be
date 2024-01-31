package com.salmalteam.salmal.notification.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.salmalteam.salmal.common.entity.BaseCreatedTimeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notification extends BaseCreatedTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long memberId;
	private String uuid;
	private String message;
	private Long markId;
	@Enumerated(value = EnumType.STRING)
	private Type type;
	private boolean isRead;
	private String memberImageUrl;
	private String markContentImageUrl;

	public static Notification createNewReplyType(Long memberId, Long markId, UUID uuid, String message,
		String memberImageUrl, String markContentImageUrl) {
		return new Notification(null, memberId, uuid.toString(), message, markId, Type.REPLY, false, memberImageUrl,
			markContentImageUrl);
	}

	public void read() {
		isRead = true;
	}
}
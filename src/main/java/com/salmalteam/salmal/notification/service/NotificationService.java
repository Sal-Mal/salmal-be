package com.salmalteam.salmal.notification.service;

import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salmalteam.salmal.common.support.UuidGenerator;
import com.salmalteam.salmal.member.application.MemberService;
import com.salmalteam.salmal.notification.dto.MessageSpec;
import com.salmalteam.salmal.notification.dto.response.FindNotificationResponse;
import com.salmalteam.salmal.notification.dto.response.NotificationDto;
import com.salmalteam.salmal.notification.entity.MemberNotification;
import com.salmalteam.salmal.notification.entity.Notification;
import com.salmalteam.salmal.notification.infra.MemberNotificationRepository;
import com.salmalteam.salmal.notification.infra.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final MemberNotificationRepository memberNotificationRepository;
	private final MemberService memberService;
	private final UuidGenerator uuidGenerator;

	@Transactional(readOnly = true)
	public FindNotificationResponse findAll(Long memberId) {
		validateMember(memberId);
		return new FindNotificationResponse(notificationRepository.findByMemberId(memberId).stream()
			.map(NotificationDto::create)
			.collect(Collectors.toList()));
	}

	@Transactional
	public void delete(Long memberId, String uuid) {
		validateMember(memberId);
		notificationRepository.deleteByUuid(uuid);
	}

	@Transactional
	public void read(Long memberId, String uuid) {
		validateMember(memberId);
		notificationRepository.findByUuid(uuid)
			.orElseThrow(() -> new IllegalArgumentException("해당하는 알림이 존재하지 않습니다."))
			.read();
	}

	private void validateMember(Long memberId) {
		memberService.findMemberById(memberId);
	}

	@Transactional
	public MessageSpec save(Long targetId, Long issuedContentId, String nickName, String content) {

		String message = String.format("%s님의 답댓글:%s", nickName, content);

		String token = memberNotificationRepository.findByMemberId(targetId)
			.map(MemberNotification::getNotificationToken)
			.orElse("token");

		Notification notification = notificationRepository.save(
			Notification.createNewReplyType(targetId, issuedContentId, uuidGenerator.generate(), message));

		HashMap<String, String> data = new HashMap<>();

		data.put("issuedContent", issuedContentId.toString());
		data.put("notificationId", notification.getUuid());
		data.put("createdAt", notification.getCreatedAt().toString());

		return new MessageSpec(token, "살말", message, data);
	}
}

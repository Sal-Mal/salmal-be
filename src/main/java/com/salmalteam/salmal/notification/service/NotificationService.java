package com.salmalteam.salmal.notification.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.salmalteam.salmal.member.application.MemberService;
import com.salmalteam.salmal.notification.dto.response.FindNotificationResponse;
import com.salmalteam.salmal.notification.dto.response.NotificationDto;
import com.salmalteam.salmal.notification.infra.NotificationRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final MemberService memberService;

	@Transactional(readOnly = true)
	public FindNotificationResponse findAll(Long memberId) {
		return new FindNotificationResponse(notificationRepository.findByMemberId(memberId).stream()
			.map(NotificationDto::create)
			.collect(Collectors.toList()));
	}

	@Transactional
	public void delete(Long memberId, String uuid) {
		validateMember(memberId);
		notificationRepository.deleteByUuid(uuid);
	}

	private void validateMember(Long memberId) {
		memberService.findMemberById(memberId);
	}

	public void read(Long memberId, String uuid) {

	}
}

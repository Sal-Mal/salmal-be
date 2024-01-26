package com.salmalteam.salmal.notification.service;

import org.springframework.stereotype.Service;

import com.salmalteam.salmal.member.application.MemberService;
import com.salmalteam.salmal.notification.entity.MemberNotification;
import com.salmalteam.salmal.notification.infra.MemberNotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberNotificationService {

	private final MemberService memberService;
	private final MemberNotificationRepository memberNotificationRepository;

	public void addToken(Long memberId, String token) {
		validateMember(memberId);
		if (memberNotificationRepository.existsByMemberId(memberId)) {
			updateToken(memberId, token);
			return;
		}
		memberNotificationRepository.save(MemberNotification.create(memberId, token));
	}

	private void validateMember(Long memberId) {
		memberService.findMemberById(memberId);
	}

	private void updateToken(Long memberId, String token) {
		MemberNotification memberNotification = memberNotificationRepository.findByMemberId(memberId)
			.orElseThrow(() -> new IllegalArgumentException("토큰 업데이트 도중 문제가 발생 했습니다."));
		memberNotification.updateToken(token);
	}
}

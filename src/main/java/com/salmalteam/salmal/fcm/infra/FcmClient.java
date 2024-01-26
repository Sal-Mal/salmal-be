package com.salmalteam.salmal.fcm.infra;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.salmalteam.salmal.notification.dto.MessageSpec;
import com.salmalteam.salmal.notification.service.NotificationPublisher;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FcmClient implements NotificationPublisher {

	@Async
	@Override
	public void pub(MessageSpec messageSpec) {
		Notification notification = Notification.builder()
			.setTitle(messageSpec.getTitle())
			.setBody(messageSpec.getBody())
			.build();
		Message message = Message.builder()
			.setNotification(notification)
			.putAllData(messageSpec.getData())
			.setToken(messageSpec.getToken())
			.build();
		try {
			String response = FirebaseMessaging.getInstance().send(message);
			log.info("Successfully sent message: {}", response);
		} catch (FirebaseMessagingException e) {
			log.error("Failed sent message: {}", e.getMessage());
			throw new IllegalArgumentException(e);
		}
	}
}
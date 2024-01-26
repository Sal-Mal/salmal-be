package com.salmalteam.salmal.notification.service;

import com.salmalteam.salmal.notification.dto.MessageSpec;

public interface NotificationPublisher {
	void pub(MessageSpec messageSpec, String token);
}

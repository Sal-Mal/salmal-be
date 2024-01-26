package com.salmalteam.salmal.notification.infra;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salmalteam.salmal.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
	List<Notification> findByMemberId(Long memberId);

	void deleteByUuid(String uuid);
}

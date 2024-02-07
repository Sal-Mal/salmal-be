package com.salmalteam.salmal.notification.infra;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.salmalteam.salmal.notification.entity.Notification;
import com.salmalteam.salmal.notification.entity.Type;
import com.salmalteam.salmal.support.RepositoryTest;

class NotificationRepositoryTest extends RepositoryTest {

	@Autowired
	NotificationRepository notificationRepository;
	@Autowired
	TestEntityManager testEntityManager;

	@Test
	void deleteByUUID() {
		//given
		EntityManager entityManager = testEntityManager.getEntityManager();
		String testImageUrl = "https://healthix.org/wp-content/uploads/2016/06/testimage.jpeg";
		UUID uuid = UUID.randomUUID();
		Notification entity = new Notification(null, 1000L, uuid.toString(), "뭐시기님의 대댓글 알림:ㅎㅇ", 2050L, Type.REPLY, true,
			testImageUrl, testImageUrl, 563L);

		entityManager.persist(entity);
		entityManager.clear();

		//when
		notificationRepository.deleteByUuid(uuid.toString());

		//then
		List<Notification> actual = entityManager.createQuery("select n from Notification n where n.uuid=:uuid",
				Notification.class)
			.setParameter("uuid", uuid.toString())
			.getResultList();

		assertThat(actual.isEmpty()).isTrue();
	}

	@Test
	void findByMemberId() {
		//given
		EntityManager entityManager = testEntityManager.getEntityManager();
		String testImageUrl = "https://healthix.org/wp-content/uploads/2016/06/testimage.jpeg";
		Notification entity1 = new Notification(null, 1000L, UUID.randomUUID().toString(), "뭐시기님의 대댓글 알림:ㅎㅇ", 2050L, Type.REPLY,
			false,
			testImageUrl, testImageUrl, 563L);
		entityManager.persist(entity1);

		Notification entity2 = new Notification(null, 1000L, UUID.randomUUID().toString(), "뭐시기님의 대댓글 알림:ㅎㅇ2", 2050L, Type.REPLY,
			true,
			testImageUrl, testImageUrl, 563L);
		entityManager.persist(entity2);

		Notification entity3 = new Notification(null, 120L, UUID.randomUUID().toString(), "뭐시기님의 대댓글 알림:ㅎㅇ3", 555L, Type.REPLY, true,
			testImageUrl, testImageUrl, 573L);
		entityManager.persist(entity3);

		entityManager.clear();

		//when
		List<Notification> actual = notificationRepository.findByMemberId(1000L);

		//then
		assertThat(actual).hasSize(2)
			.extracting("memberId")
			.containsOnly(1000L);
	}

	@Test
	void findByUuid() {
		//given
		EntityManager entityManager = testEntityManager.getEntityManager();
		String testImageUrl = "https://healthix.org/wp-content/uploads/2016/06/testimage.jpeg";
		String uuid = UUID.randomUUID().toString();
		Notification entity1 = new Notification(null, 1000L, uuid, "뭐시기님의 대댓글 알림:ㅎㅇ", 2050L, Type.REPLY,
			false,
			testImageUrl, testImageUrl, 563L);
		entityManager.persist(entity1);

		Notification entity2 = new Notification(null, 1000L, UUID.randomUUID().toString(), "뭐시기님의 대댓글 알림:ㅎㅇ2", 2050L, Type.REPLY,
			true,
			testImageUrl, testImageUrl, 563L);
		entityManager.persist(entity2);

		Notification entity3 = new Notification(null, 120L, UUID.randomUUID().toString(), "뭐시기님의 대댓글 알림:ㅎㅇ3", 555L, Type.REPLY, true,
			testImageUrl, testImageUrl, 573L);
		entityManager.persist(entity3);
		entityManager.clear();

		//when
		Notification actual = notificationRepository.findByUuid(uuid).get();

		//then
		assertThat(actual).isNotNull();
		assertThat(actual.getUuid()).isEqualTo(uuid);
		assertThat(actual.getContentId()).isEqualTo(563L);
		assertThat(actual.getMarkId()).isEqualTo(2050L);
		assertThat(actual.getMessage()).isEqualTo("뭐시기님의 대댓글 알림:ㅎㅇ");
		assertThat(actual.getMemberId()).isEqualTo(1000L);
		assertThat(actual.getMemberImageUrl()).isEqualTo(testImageUrl);
		assertThat(actual.getMarkContentImageUrl()).isEqualTo(testImageUrl);
		assertThat(actual.getType()).isEqualTo(Type.REPLY);
		assertThat(actual.getCreatedAt()).isNotNull();
	}
}
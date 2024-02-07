package com.salmalteam.salmal.presentation.http.notification;

import static com.salmalteam.salmal.notification.entity.Type.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.salmalteam.salmal.notification.dto.request.ReadNotificationRequest;
import com.salmalteam.salmal.notification.dto.response.FindNotificationResponse;
import com.salmalteam.salmal.notification.dto.response.NotificationDto;
import com.salmalteam.salmal.notification.service.DeleteNotificationRequest;
import com.salmalteam.salmal.support.PresentationTest;

class NotificationControllerTest extends PresentationTest {

	private final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

	@Test
	@DisplayName("알림이 조회되어야 한다.")
	void findNotification() throws Exception {
		String testImageUrl = "https://healthix.org/wp-content/uploads/2016/06/testimage.jpeg";

		List<NotificationDto> notifications = List.of(
			createNotificationDto(10L, "대댓글1", false, "memberImageUrl", testImageUrl, 150L),
			createNotificationDto(10L, "대댓글2", false, "memberImageUrl", testImageUrl, 150L),
			createNotificationDto(15L, "대댓글3", true, "memberImageUrl", testImageUrl, 155L),
			createNotificationDto(15L, "대댓글4", true, "memberImageUrl", testImageUrl, 155L));

		given(notificationService.findAll(any())).willReturn(new FindNotificationResponse(notifications));
		mockingForAuthorization();

		//expect
		mockMvc.perform(get("/api/notification")
				.header("Authorization", "Bearer " + accessToken)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.notifications").isArray())

			.andExpect(jsonPath("$.notifications[0].markId").value(10L))
			.andExpect(jsonPath("$.notifications[0].uuid").isNotEmpty())
			.andExpect(jsonPath("$.notifications[0].type").value("REPLY"))
			.andExpect(jsonPath("$.notifications[0].message").value("대댓글1"))
			.andExpect(jsonPath("$.notifications[0].createAt").isNotEmpty())
			.andExpect(jsonPath("$.notifications[0].memberImageUrl").isNotEmpty())
			.andExpect(jsonPath("$.notifications[0].imageUrl").value(testImageUrl))
			.andExpect(jsonPath("$.notifications[0].contentId").value(150L))
			.andExpect(jsonPath("$.notifications[0].read").value(false))

			.andExpect(jsonPath("$.notifications[1].markId").value(10L))
			.andExpect(jsonPath("$.notifications[1].uuid").isNotEmpty())
			.andExpect(jsonPath("$.notifications[1].type").value("REPLY"))
			.andExpect(jsonPath("$.notifications[1].message").value("대댓글2"))
			.andExpect(jsonPath("$.notifications[1].createAt").isNotEmpty())
			.andExpect(jsonPath("$.notifications[1].memberImageUrl").isNotEmpty())
			.andExpect(jsonPath("$.notifications[1].imageUrl").value(testImageUrl))
			.andExpect(jsonPath("$.notifications[1].contentId").value(150L))
			.andExpect(jsonPath("$.notifications[1].read").value(false))

			.andExpect(jsonPath("$.notifications[2].markId").value(15L))
			.andExpect(jsonPath("$.notifications[2].uuid").isNotEmpty())
			.andExpect(jsonPath("$.notifications[2].type").value("REPLY"))
			.andExpect(jsonPath("$.notifications[2].message").value("대댓글3"))
			.andExpect(jsonPath("$.notifications[2].createAt").isNotEmpty())
			.andExpect(jsonPath("$.notifications[2].memberImageUrl").isNotEmpty())
			.andExpect(jsonPath("$.notifications[2].imageUrl").value(testImageUrl))
			.andExpect(jsonPath("$.notifications[2].contentId").value(155L))
			.andExpect(jsonPath("$.notifications[2].read").value(true))

			.andExpect(jsonPath("$.notifications[3].markId").value(15L))
			.andExpect(jsonPath("$.notifications[3].uuid").isNotEmpty())
			.andExpect(jsonPath("$.notifications[3].type").value("REPLY"))
			.andExpect(jsonPath("$.notifications[3].message").value("대댓글4"))
			.andExpect(jsonPath("$.notifications[3].createAt").isNotEmpty())
			.andExpect(jsonPath("$.notifications[3].memberImageUrl").isNotEmpty())
			.andExpect(jsonPath("$.notifications[3].imageUrl").value(testImageUrl))
			.andExpect(jsonPath("$.notifications[3].contentId").value(155L))
			.andExpect(jsonPath("$.notifications[3].read").value(true))

			.andDo(restDocs.document(
				requestHeaders(
					headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입의 인증용 JWT 토큰")
				)
			));

		then(notificationService).should(times(1)).findAll(any());
	}

	@Test
	@DisplayName("알림이 삭제되어야 한다.")
	void deleteNotification() throws Exception {
		//given
		mockingForAuthorization();
		DeleteNotificationRequest request = new DeleteNotificationRequest(UUID.randomUUID().toString());

		//expect

		mockMvc.perform(delete("/api/notification")
				.header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(createJson(request))
			)
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입의 인증용 JWT 토큰")),
				PayloadDocumentation.requestFields(
					PayloadDocumentation.fieldWithPath("uuid").type(String.class).description("알림 고유 번호")
				)
			));

		then(notificationService).should(times(1)).delete(anyLong(), anyString());
	}

	@Test
	@DisplayName("알림 삭제 시 uuid 없으면 예외 발생")
	void delete_notification_invalid_request() throws Exception{
		//given
		mockingForAuthorization();
		DeleteNotificationRequest request = new DeleteNotificationRequest("");

		//expect
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/notification")
				.header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(createJson(request))
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.[0].message").value("알림의 고유번호는 필수로 필요합니다."))
			.andExpect(jsonPath("$.[0].code").value(0));

		then(notificationService).should(times(0)).findAll(any());
	}

	@Test
	@DisplayName("알림 읽기상태를 읽음으로 변경한다.")
	void readNotification() throws Exception {
		mockingForAuthorization();
		ReadNotificationRequest request = new ReadNotificationRequest(UUID.randomUUID().toString());

		//expect

		mockMvc.perform(post("/api/notification/read")
				.header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(createJson(request))
			)
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestHeaders(
					headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입의 인증용 JWT 토큰")),
				PayloadDocumentation.requestFields(
					PayloadDocumentation.fieldWithPath("uuid").type(String.class).description("알림 고유 번호")
				)
			));

		then(notificationService).should(times(1)).read(anyLong(), anyString());
	}

	@Test
	@DisplayName("알림 읽기 상태 변경 시 uuid 없으면 예외 발생")
	void read_notification_invalid_request() throws Exception{
		//given
		mockingForAuthorization();
		DeleteNotificationRequest request = new DeleteNotificationRequest("");

		//expect
		mockMvc.perform(MockMvcRequestBuilders.post("/api/notification/read")
				.header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(createJson(request))
			)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.[0].message").value("알림의 고유번호는 필수로 필요합니다."))
			.andExpect(jsonPath("$.[0].code").value(0));

		then(notificationService).should(times(0)).findAll(any());
	}

	private static NotificationDto createNotificationDto(long markId, String testMessage, boolean isRead,
		String memberImageUrl, String imageUrl, long contentId) {
		return new NotificationDto(UUID.randomUUID().toString(), markId, REPLY, testMessage, isRead,
			LocalDateTime.now(),
			memberImageUrl,
			imageUrl, contentId);
	}

}
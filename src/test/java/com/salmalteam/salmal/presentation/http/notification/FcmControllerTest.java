package com.salmalteam.salmal.presentation.http.notification;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.salmalteam.salmal.fcm.dto.request.AddFcmRequest;
import com.salmalteam.salmal.support.PresentationTest;

class FcmControllerTest extends PresentationTest {

	private final String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

	@Test
	@DisplayName("FCM 토큰을 저장 해야한다.")
	void addFcm() throws Exception {
		//given
		String token = "FCMFCMFCMFCMFCMFCMFCMFCMFCMFCMFCMFCMFCMFCMFCMFCMFCMFCM";
		AddFcmRequest request = new AddFcmRequest(token);
		mockingForAuthorization();

		//expect
		mockMvc.perform(post("/api/fcm/add-token")
				.header("Authorization", "Bearer " + accessToken)
				.content(createJson(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입의 인증용 JWT 토큰")),
				requestFields(fieldWithPath("token").type(JsonFieldType.STRING).description("FCM 에서 발급받은 토큰"))
			));
		then(memberNotificationService).should(times(1)).addToken(anyLong(), anyString());
	}

	@Test
	@DisplayName("token 이 비어있을 시 예외가 발생한다.")
	void addFcm_invalid_token() throws Exception {
		//given
		AddFcmRequest request = new AddFcmRequest("");
		mockingForAuthorization();

		//expect
		mockMvc.perform(MockMvcRequestBuilders.post("/api/fcm/add-token")
				.header("Authorization", "Bearer " + accessToken)
				.content(createJson(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$[0].message").value("FCM 토큰은 필수적으로 필요합니다."))
			.andExpect(jsonPath("$[0].code").value(0));

		then(memberNotificationService).should(times(0)).addToken(anyLong(), anyString());
	}
}
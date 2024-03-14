package com.salmalteam.salmal.presentation.http.vote;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.salmalteam.salmal.support.PresentationTest;
import com.salmalteam.salmal.vote.dto.request.VoteReportRequest;

class VoteV2ControllerTest extends PresentationTest {

	private static final String BASE_URL = "/api/v2/votes";
	private final static String URL = "/{vote-id}/reports";

	@Test
	void 투표_신고_성공() throws Exception {
		// given
		final Long voteId = 1L;
		mockingForAuthorization();

		VoteReportRequest voteReportRequest = new VoteReportRequest("부적절한 내용");
		// when
		final ResultActions resultActions = mockMvc.perform(
				RestDocumentationRequestBuilders.post(BASE_URL + URL, voteId)
					.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
					.characterEncoding(StandardCharsets.UTF_8)
					.content(createJson(voteReportRequest))
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isCreated());

		// then
		resultActions.andDo(restDocs.document(
			requestHeaders(
				headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
			),
			pathParameters(
				parameterWithName("vote-id").description("신고할 투표 ID")
			),
			requestFields(
				fieldWithPath("reason").type(JsonFieldType.STRING).description("투표 신고 사유")
			)
		));

		verify(voteService, times(1)).report(any(), eq(voteId), anyString());

	}

	@Test
	void 미인증_사용자일_경우_401_응답() throws Exception {

		// given
		final Long voteId = 1L;
		// when & then
		mockMvc.perform(post(BASE_URL + URL, voteId)
				.characterEncoding(StandardCharsets.UTF_8)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
	}

}
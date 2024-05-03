package com.salmalteam.salmal.presentation.http.member;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import com.salmalteam.salmal.member.dto.response.MyPageV2Response;
import com.salmalteam.salmal.support.PresentationTest;

class MemberV2ControllerTest extends PresentationTest {

	private static final String BASE_URL = "/api/v2/members";

	@Test
	@DisplayName("마이페이지 조회 v2 api 테스트")
	void findMyPageV2() throws Exception {
		// given
		final Long searchMemberId = 1L;
		final MyPageV2Response myPageResponse = new MyPageV2Response(1L, "imageUrl", "사과나무", "안녕하세요!", 11, 8, false,
			5L);

		given(memberService.findMemberProfile(anyLong(), anyLong()))
			.willReturn(myPageResponse);

		mockingForAuthorization();

		// when
		final ResultActions resultActions = mockMvc.perform(
				get(BASE_URL + "/{searchMemberId}", searchMemberId)
					.header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
					.characterEncoding(StandardCharsets.UTF_8)
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.imageUrl").value("imageUrl"))
			.andExpect(jsonPath("$.nickName").value("사과나무"))
			.andExpect(jsonPath("$.introduction").value("안녕하세요!"))
			.andExpect(jsonPath("$.likeCount").value(11))
			.andExpect(jsonPath("$.disLikeCount").value(8))
			.andExpect(jsonPath("$.blocked").value(false))
			.andExpect(jsonPath("$.totalVoteCount").value(5));

		// then
		resultActions.andDo(restDocs.document(
			requestHeaders(
				headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
			),
			requestParameters(
				parameterWithName("member-id").optional().description("마이페이지 조회할 회원 ID")
			),
			responseFields(
				fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원 ID"),
				fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
				fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임"),
				fieldWithPath("introduction").type(JsonFieldType.STRING).description("한 줄 소개"),
				fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("총 받은 좋아요 수"),
				fieldWithPath("disLikeCount").type(JsonFieldType.NUMBER).description("총 받은 싫어요 수"),
				fieldWithPath("blocked").type(JsonFieldType.BOOLEAN).description("차단 여부"),
				fieldWithPath("totalVoteCount").type(JsonFieldType.NUMBER).description("사용자가 등록한 투표 수")
			)
		));

	}
}
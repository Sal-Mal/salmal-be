package com.salmalteam.salmal.presentation.member;

import com.salmalteam.salmal.dto.response.member.MyPageResponse;
import com.salmalteam.salmal.support.PresentationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends PresentationTest {

    private static final String BASE_URL = "/api/members";

    @Nested
    class 마이페이지_조회{

        private static final String URL = "/{member-id}";

        @Test
        void 마이페이지_조회_성공() throws Exception{
            // given
            final Long memberId = 1L;
            final MyPageResponse myPageResponse = new MyPageResponse(1L, "imageUrl", "사과나무", "안녕하세요!", 11, 8, false);

            given(memberService.findMyPage(eq(memberId))).willReturn(myPageResponse);
            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + URL, memberId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

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
                            fieldWithPath("blocked").type(JsonFieldType.BOOLEAN).description("차단 여부")
                    )
            ));

        }

        @Test
        void 미인증_사용자일_경우_401_응답() throws Exception {

            // given
            final Long memberId = 1L;

            // when & then
            mockMvc.perform(get(BASE_URL + URL, memberId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

}
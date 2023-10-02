package com.salmalteam.salmal.presentation.member;

import com.salmalteam.salmal.dto.request.member.MyPageUpdateRequest;
import com.salmalteam.salmal.dto.response.member.MyPageResponse;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import com.salmalteam.salmal.support.PresentationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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

    @Nested
    class 회원_차단{
        private static final String URL = "/{member-id}/blocks";
        @Test
        void 회원_차단_성공() throws Exception{
            // given
            final Long memberId = 1L;
            final MemberPayLoad memberPayLoad = MemberPayLoad.from(memberId);

            mockingForAuthorization();
            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + URL, memberId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isCreated());
            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    requestParameters(
                            parameterWithName("member-id").optional().description("차단할 회원 ID")
                    )
            ));

        }

        @Test
        void 미인증_사용자일_경우_401_응답() throws Exception {

            // given
            final Long memberId = 1L;

            // when & then
            mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + URL, memberId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class 회원_차단_취소{
        private static final String URL = "/{member-id}/blocks";
        @Test
        void 회원_차단_취소_성공() throws Exception{
            // given
            final Long memberId = 1L;

            mockingForAuthorization();
            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.delete(BASE_URL + URL, memberId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());

            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    requestParameters(
                            parameterWithName("member-id").optional().description("차단을 취소할 회원 ID")
                    )
            ));

        }

        @Test
        void 미인증_사용자일_경우_401_응답() throws Exception {

            // given
            final Long memberId = 1L;

            // when & then
            mockMvc.perform(RestDocumentationRequestBuilders.delete(BASE_URL + URL, memberId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class 마이페이지_수정{

        private static final String URL = "/{member-id}";
        @Test
        void 마이페이지_수정_성공() throws Exception {

            final Long memberId = 1L;

            final String nickName = "수정할 닉네임";
            final String introduction = "수정할 한줄 소개";
            final MyPageUpdateRequest myPageUpdateRequest = new MyPageUpdateRequest(nickName, introduction);

            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(put(BASE_URL + URL, memberId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(createJson(myPageUpdateRequest))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    pathParameters(
                            parameterWithName("member-id").description("마이페이지 수정할 회원 ID")
                    ),
                    requestFields(
                            fieldWithPath("nickName").type(JsonFieldType.STRING).description("수정할 닉네임"),
                            fieldWithPath("introduction").type(JsonFieldType.STRING).description("수정할 한줄 소개")
                    )
            ));

            verify(memberService).updateMyPage(any(),any(),any());
        }

        @Test
        void 미인증_사용자일_경우_401_응답() throws Exception {

            // given
            final Long memberId = 1L;

            // when & then
            mockMvc.perform(put(BASE_URL + URL, memberId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class 회원_이미지_수정{

        private static final String URL = "/{member-id}/images";
        @Test
        void 회원_이미지_수정_성공() throws Exception{

            // given
            final Long memberId = 1L;

            final String name = "imageFile";
            final String fileName = "testImage.jpg";
            final String filePath = "src/test/resources/testImages/".concat(fileName);
            final FileInputStream fileInputStream = new FileInputStream(filePath);
            final String contentType = "image/jpeg";
            final MockMultipartFile multipartFile = new MockMultipartFile(name, fileName, contentType, fileInputStream);

            mockingForAuthorization();
            // when
            final ResultActions resultActions = mockMvc.perform(multipart(BASE_URL + URL, memberId)
                            .file(multipartFile)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    pathParameters(
                            parameterWithName("member-id").description("이미지 수정할 회원 ID")
                    ),
                    requestParts(
                            partWithName("imageFile").description("업로드할 회원 이미지(jpg, jpeg)")
                    )
            ));

            verify(memberService).updateImage(any(), any(), any());
        }

        @Test
        void 미인증_사용자일_경우_401_응답() throws Exception {

            // given
            final Long memberId = 1L;

            // when & then
            mockMvc.perform(multipart(BASE_URL + URL, memberId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }


        @Test
        void 이미지_파일이_없는_경우_400_응답() throws Exception {

            // given
            final Long memberId = 1L;

            mockingForAuthorization();

            // when & then
            mockMvc.perform(multipart(BASE_URL + URL, memberId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

}
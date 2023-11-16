package com.salmalteam.salmal.presentation.member;

import com.salmalteam.salmal.dto.request.member.MyPageUpdateRequest;
import com.salmalteam.salmal.dto.response.member.MyPageResponse;
import com.salmalteam.salmal.dto.response.member.block.MemberBlockedPageResponse;
import com.salmalteam.salmal.dto.response.member.block.MemberBlockedResponse;
import com.salmalteam.salmal.dto.response.member.vote.*;
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
import java.time.LocalDateTime;
import java.util.List;

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
    class 마이페이지_조회 {

        private static final String URL = "/{member-id}";

        @Test
        void 마이페이지_조회_성공() throws Exception {
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
    class 회원_차단 {
        private static final String URL = "/{member-id}/blocks";

        @Test
        void 회원_차단_성공() throws Exception {
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
    class 회원_차단_취소 {
        private static final String URL = "/{member-id}/blocks";

        @Test
        void 회원_차단_취소_성공() throws Exception {
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
    class 마이페이지_수정 {

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

            verify(memberService).updateMyPage(any(), any(), any());
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
    class 회원_이미지_수정 {

        private static final String URL = "/{member-id}/images";

        @Test
        void 회원_이미지_수정_성공() throws Exception {

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

    @Nested
    class 회원_차단_목록_조회 {

        private static final String URL = "/{member-id}/blocks";

        @Test
        void 회원_차단_목록_조회_성공() throws Exception {
            // given
            final Long memberId = 1L;
            final Integer size = 2;
            final Long cursorId = 10L;


            final MemberBlockedResponse memberBlockedResponse1 = new MemberBlockedResponse(9L, "사과나무", "imageUrl", LocalDateTime.now());
            final MemberBlockedResponse memberBlockedResponse2 = new MemberBlockedResponse(8L, "포두나무", "imageUrl", LocalDateTime.now());
            final MemberBlockedPageResponse memberBlockedPageResponse = MemberBlockedPageResponse.of(true, List.of(memberBlockedResponse1, memberBlockedResponse2));

            given(memberService.searchBlockedMembers(any(), any(), any())).willReturn(memberBlockedPageResponse);

            mockingForAuthorization();
            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + URL, memberId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .param("size", String.valueOf(size))
                            .param("cursor-id", String.valueOf(cursorId))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    pathParameters(
                            parameterWithName("member-id").description("차단 목록을 조회할 회원 ID")
                    ),
                    requestParameters(
                            parameterWithName("cursor-id").optional().description("이전 마지막 조회 결과 회원 ID (첫 페이지 조회 시 입력 X)"),
                            parameterWithName("size").optional().description("검색할 ROW 수")
                    ),
                    responseFields(
                            fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부 "),
                            subsectionWithPath("blockedMembers").type(JsonFieldType.ARRAY).description("차단 회원 목록")
                    )
            )).andDo(restDocs.document(
                            responseFields(beneathPath("blockedMembers").withSubsectionId("blockedMembers"),
                                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원 ID"),
                                    fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
                                    fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임"),
                                    fieldWithPath("blockedDate").type(JsonFieldType.STRING).description("차단일")
                            )
                    )
            );
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
    class 회원_투표_목록_조회_테스트{
        private static final String URL = "/{member-id}/votes";

        @Test
        void 회원_투표_목록_조회_성공() throws Exception{
            // given
            final Long memberId = 1L;
            final Integer size = 2;
            final Long cursorId = 10L;

            final MemberVoteResponse memberVoteResponse1 = new MemberVoteResponse(9L, "imageUrl", LocalDateTime.now());
            final MemberVoteResponse memberVoteResponse2 = new MemberVoteResponse(8L, "imageUrl", LocalDateTime.now());
            final MemberVotePageResponse memberVotePageResponse = MemberVotePageResponse.of(true, List.of(memberVoteResponse1, memberVoteResponse2));
            given(memberService.searchMemberVotes(any(),any(),any())).willReturn(memberVotePageResponse);

            mockingForAuthorization();
            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + URL, memberId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .param("size", String.valueOf(size))
                            .param("cursor-id", String.valueOf(cursorId))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    pathParameters(
                            parameterWithName("member-id").description("투표 목록을 조회할 회원 ID")
                    ),
                    requestParameters(
                            parameterWithName("cursor-id").optional().description("이전 마지막 조회 결과 투표 ID (첫 페이지 조회 시 입력 X)"),
                            parameterWithName("size").optional().description("검색할 ROW 수")
                    ),
                    responseFields(
                            fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부 "),
                            subsectionWithPath("votes").type(JsonFieldType.ARRAY).description("차단 회원 목록")
                    )
            )).andDo(restDocs.document(
                            responseFields(beneathPath("votes").withSubsectionId("votes"),
                                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("투표 ID"),
                                    fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("투표 이미지 URL"),
                                    fieldWithPath("createdDate").type(JsonFieldType.STRING).description("생성일")
                            )
                    )
            );
        }

        @Test
        void 미인증_사용자일_경우_401_응답() throws Exception{
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
    class 회원_평가_투표_목록_조회{

        private final String URL = "/{member-id}/evaluations";

        @Test
        void 회원_평가_투표_목록_조회_성공() throws Exception{
            // given
            final Long memberId = 1L;
            final Integer size = 2;
            final Long cursorId = 10L;

            mockingForAuthorization();

            MemberEvaluationVoteResponse memberEvaluationVoteResponse1 = new MemberEvaluationVoteResponse(4L, "imageUrl", LocalDateTime.now());
            MemberEvaluationVoteResponse memberEvaluationVoteResponse2 = new MemberEvaluationVoteResponse(2L, "imageUrl", LocalDateTime.now());

            MemberEvaluationVotePageResponse memberEvaluationVotePageResponse = MemberEvaluationVotePageResponse.of(true, List.of(memberEvaluationVoteResponse1, memberEvaluationVoteResponse2));
            given(memberService.searchMemberEvaluatedVotes(any(), any(), any())).willReturn(memberEvaluationVotePageResponse);

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + URL, memberId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .param("size", String.valueOf(size))
                            .param("cursor-id", String.valueOf(cursorId))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    pathParameters(
                            parameterWithName("member-id").description("평가 목록을 조회할 회원 ID")
                    ),
                    requestParameters(
                            parameterWithName("cursor-id").optional().description("이전 마지막 조회 결과 투표 ID (첫 페이지 조회 시 입력 X)"),
                            parameterWithName("size").optional().description("검색할 ROW 수")
                    ),
                    responseFields(
                            fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부"),
                            subsectionWithPath("votes").type(JsonFieldType.ARRAY).description("평가 투표 목록")
                    )
            )).andDo(restDocs.document(
                            responseFields(beneathPath("votes").withSubsectionId("votes"),
                                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("투표 ID"),
                                    fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("투표 이미지 URL"),
                                    fieldWithPath("createdDate").type(JsonFieldType.STRING).description("평가 생성일")
                            )
                    )
            );

        }
    }

    @Nested
    class 회원_북마크_투표_목록_조회{
        private final String URL = "/{member-id}/bookmarks";

        @Test
        void 회원_북마크_투표_목록_조회_성공() throws Exception{

            // given
            final Long memberId = 1L;
            final Integer size = 2;
            final Long cursorId = 10L;

            mockingForAuthorization();

            MemberBookmarkVoteResponse memberBookmarkVoteResponse1 = new MemberBookmarkVoteResponse(40L, "imageUrl", LocalDateTime.now());
            MemberBookmarkVoteResponse memberBookmarkVoteResponse2 = new MemberBookmarkVoteResponse(24L, "imageUrl", LocalDateTime.now());

            MemberBookmarkVotePageResponse memberBookmarkVotePageResponse = MemberBookmarkVotePageResponse.of(true, List.of(memberBookmarkVoteResponse1, memberBookmarkVoteResponse2));
            given(memberService.searchMemberBookmarkedVotes(any(), any(), any())).willReturn(memberBookmarkVotePageResponse);

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + URL, memberId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .param("size", String.valueOf(size))
                            .param("cursor-id", String.valueOf(cursorId))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    pathParameters(
                            parameterWithName("member-id").description("북마크 목록을 조회할 회원 ID")
                    ),
                    requestParameters(
                            parameterWithName("cursor-id").optional().description("이전 마지막 조회 결과 투표 ID (첫 페이지 조회 시 입력 X)"),
                            parameterWithName("size").optional().description("검색할 ROW 수")
                    ),
                    responseFields(
                            fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부"),
                            subsectionWithPath("votes").type(JsonFieldType.ARRAY).description("평가 투표 목록")
                    )
            )).andDo(restDocs.document(
                            responseFields(beneathPath("votes").withSubsectionId("votes"),
                                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("투표 ID"),
                                    fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("투표 이미지 URL"),
                                    fieldWithPath("createdDate").type(JsonFieldType.STRING).description("북마크 생성일")
                            )
                    )
            );
        }
    }

    @Nested
    class 회원_삭제{
        private final String URL = "/{member-id}";
        @Test
        void 회원_삭제_성공() throws Exception{
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
                    pathParameters(
                            parameterWithName("member-id").description("삭제할 회원 ID")
                    )
            ));

        }
    }

    @Nested
    class 회원_프로필_이미지_삭제{
        private final String URL = "/{member-id}/images";
        @Test
        void 회원_프로필_이미지_삭제_성공() throws Exception{
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
                    pathParameters(
                            parameterWithName("member-id").description("프로필 이미지를 삭제할 회원 ID")
                    )
            ));

        }
    }

}
package com.salmalteam.salmal.presentation.vote;

import com.salmalteam.salmal.dto.request.vote.VoteBookmarkRequest;
import com.salmalteam.salmal.dto.request.vote.VoteCommentCreateRequest;
import com.salmalteam.salmal.dto.request.vote.VoteEvaluateRequest;
import com.salmalteam.salmal.dto.request.vote.VotePageRequest;
import com.salmalteam.salmal.dto.response.vote.VotePageResponse;
import com.salmalteam.salmal.dto.response.vote.VoteResponse;
import com.salmalteam.salmal.infra.auth.dto.MemberPayLoad;
import com.salmalteam.salmal.support.PresentationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends PresentationTest {

    private static final String BASE_URL = "/api/votes";

    @Nested
    class 투표_등록_테스트 {
        @Test
        void 투표_등록_성공() throws Exception {
            // given
            final String name = "imageFile";
            final String fileName = "testImage.jpg";
            final String filePath = "src/test/resources/testImages/".concat(fileName);
            final FileInputStream fileInputStream = new FileInputStream(filePath);
            final String contentType = "image/jpeg";
            final MockMultipartFile multipartFile = new MockMultipartFile(name, fileName, contentType, fileInputStream);

            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(multipart(BASE_URL)
                            .file(multipartFile)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());

            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    requestParts(
                            partWithName("imageFile").description("업로드할 투표 이미지(jpg, jpeg)")
                    )
            ));

            verify(voteService, times(1)).register(any(), any());
        }

        @Test
        void 미인증_사용자일_경우_401_응답() throws Exception {

            // when & then
            mockMvc.perform(multipart(BASE_URL)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void 이미지_파일이_없는_경우_400_응답() throws Exception {

            mockingForAuthorization();

            // when & then
            mockMvc.perform(multipart(BASE_URL)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    class 투표_평가_테스트 {

        private final static String URL = "/{vote-id}/evaluations";

        @Test
        void 투표_평가_성공() throws Exception {
            // given
            final Long voteId = 1L;
            final String voteEvaluationType = "LIKE";
            final VoteEvaluateRequest voteEvaluateRequest = new VoteEvaluateRequest(voteEvaluationType);

            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + URL, voteId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .content(createJson(voteEvaluateRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isCreated());

            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    pathParameters(
                            parameterWithName("vote-id").description("평가할 투표 ID")
                    ),
                    requestFields(
                            fieldWithPath("voteEvaluationType").type(JsonFieldType.STRING).description("투표 평가 타입 ( LIKE, DISLIKE )")
                    )
            ));

            verify(voteService, times(1)).evaluate(any(), any(), any());

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

        @ParameterizedTest
        @CsvSource(value = {"likey", "lik", "dis", "disLik"})
        void 유효한_평가_타입이_아닌_경우_400_응답(final String voteEvaluationType) throws Exception {

            // given
            final Long voteId = 1L;
            mockingForAuthorization();
            final VoteEvaluateRequest voteEvaluateRequest = new VoteEvaluateRequest(voteEvaluationType);

            // when & then
            mockMvc.perform(post(BASE_URL + URL, voteId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .content(createJson(voteEvaluateRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    class 투표_북마크_테스트 {

        private final static String URL = "/{vote-id}/bookmarks";

        @Test
        void 투표_북마킹_성공() throws Exception {
            // given
            final Long voteId = 1L;
            final Boolean isBookmarked = true;
            final VoteBookmarkRequest voteBookmarkRequest = new VoteBookmarkRequest(isBookmarked);
            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + URL, voteId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .content(createJson(voteBookmarkRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isCreated());

            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    pathParameters(
                            parameterWithName("vote-id").description("북마크할 투표 ID")
                    ),
                    requestFields(
                            fieldWithPath("isBookmarked").type(JsonFieldType.BOOLEAN).description("북마크 여부 ( true, false )")
                    )
            ));

            verify(voteService, times(1)).bookmark(any(), any(), any());

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

        @Test
        void 북마크_여부가_전달이_안된_경우_400_응답() throws Exception {

            // given
            final Long voteId = 1L;
            final Boolean isBookmarked = null;
            final VoteBookmarkRequest voteBookmarkRequest = new VoteBookmarkRequest(isBookmarked);
            mockingForAuthorization();

            // when & then
            mockMvc.perform(post(BASE_URL + URL, voteId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .content(createJson(voteBookmarkRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    class 투표_신고_테스트 {
        private final static String URL = "/{vote-id}/reports";

        @Test
        void 투표_신고_성공() throws Exception {
            // given
            final Long voteId = 1L;
            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + URL, voteId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isCreated());

            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    pathParameters(
                            parameterWithName("vote-id").description("신고할 투표 ID")
                    )
            ));

            verify(voteService, times(1)).report(any(), eq(voteId));

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

    @Nested
    class 투표_댓글_생성_테스트 {
        private final static String URL = "/{vote-id}/comments";

        @Test
        void 댓글_생성_성공() throws Exception {
            // given
            final Long voteId = 1L;
            final String content = "이 옷 정말 멋져요!";
            final VoteCommentCreateRequest voteCommentCreateRequest = new VoteCommentCreateRequest(content);
            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + URL, voteId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .content(createJson(voteCommentCreateRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isCreated());

            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    pathParameters(
                            parameterWithName("vote-id").description("댓글을 추가할 투표 ID")
                    ),
                    requestFields(
                            fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용 ( 최소 1글자 ~ 최대 100글자 ) ")
                    )
            ));

            verify(voteService, times(1)).comment(any(), any(), any());
        }

        @Test
        void 댓글을_입력하지_않은_경우_400응답() throws Exception {
            // given
            final Long voteId = 1L;
            final String content = "";
            final VoteCommentCreateRequest voteCommentCreateRequest = new VoteCommentCreateRequest(content);
            mockingForAuthorization();

            // when & then
            mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + URL, voteId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .content(createJson(voteCommentCreateRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

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

    @Nested
    class 투표_조회 {

        private final String URL = "/{vote-id}";

        @Test
        void 투표_조회_성공() throws Exception {
            // given
            final Long voteId = 1L;
            final Long memberId = 1L;
            final VoteResponse voteResponse = new VoteResponse(voteId, memberId, "imageUrl", "닉네임", "member-imageUrl", 3, 10, 10, 20, BigDecimal.valueOf(0.5), BigDecimal.valueOf(0.5), LocalDateTime.now(), true, "LIKE");
            given(voteService.search(any(), any())).willReturn(voteResponse);

            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + URL, voteId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    pathParameters(
                            parameterWithName("vote-id").description("조회할 투표 ID")
                    ),
                    responseFields(
                            fieldWithPath("id").type(JsonFieldType.NUMBER).description("투표 ID"),
                            fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("투표 작성자 ID"),
                            fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("투표 이미지 URL"),
                            fieldWithPath("nickName").type(JsonFieldType.STRING).description("투표 작성자 닉네임"),
                            fieldWithPath("memberImageUrl").type(JsonFieldType.STRING).description("투표 작성자 이미지 URL"),
                            fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("댓글 개수"),
                            fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("좋아요 개수"),
                            fieldWithPath("disLikeCount").type(JsonFieldType.NUMBER).description("싫어요 개수"),
                            fieldWithPath("totalEvaluationCnt").type(JsonFieldType.NUMBER).description("총 투표 개수"),
                            fieldWithPath("likeRatio").type(JsonFieldType.NUMBER).description("총 투표 개수"),
                            fieldWithPath("disLikeRatio").type(JsonFieldType.NUMBER).description("총 투표 개수"),
                            fieldWithPath("createdAt").type(JsonFieldType.STRING).description("투표 생성일"),
                            fieldWithPath("bookmarked").type(JsonFieldType.BOOLEAN).description("내가 북마크 했는지 여부 (true, false) "),
                            fieldWithPath("status").type(JsonFieldType.STRING).description("해당 투표에 대한 나의 상태 (NONE, LIKE, DISLIKE)")
                    )
            ));
        }

        @Test
        void 미인증_사용자일_경우_401_응답() throws Exception {

            // given
            final Long voteId = 1L;

            // when & then
            mockMvc.perform(get(BASE_URL + URL, voteId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class 투표_목록_조회 {
        @Test
        void 투표_조회_성공() throws Exception {
            // given
            final Long cursorId = 1L;
            final Integer cursorLikes = 3;
            final Integer size = 8;
            final String searchType = "BEST";
            final String imageURL = "https://.../image.jpg";

            final VoteResponse voteResponse1 = new VoteResponse(2L, 23L, imageURL, "사과", imageURL, 23, 50, 50, 100, BigDecimal.valueOf(0.5), BigDecimal.valueOf(0.5), LocalDateTime.now(), true, "NONE");
            final VoteResponse voteResponse2 = new VoteResponse(3L, 2L, imageURL, "포도", imageURL, 2, 20, 10, 30, BigDecimal.valueOf(0.67), BigDecimal.valueOf(0.33), LocalDateTime.now(), false, "LIKE");
            final VoteResponse voteResponse3 = new VoteResponse(1L, 100L, imageURL, "옥수수", imageURL, 10, 10, 10, 20, BigDecimal.valueOf(0.5), BigDecimal.valueOf(0.5), LocalDateTime.now(), true, "DISLIKE");

            final VotePageResponse votePageResponse = VotePageResponse.of(true, List.of(voteResponse1, voteResponse2, voteResponse3));

            given(voteService.searchList(any(), any(), any())).willReturn(votePageResponse);
            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .param("cursorId", String.valueOf(cursorId))
                            .param("cursorLikes", String.valueOf(cursorLikes))
                            .param("size", String.valueOf(size))
                            .param("searchType", searchType)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    requestParameters(
                            parameterWithName("cursorId").optional().description("이전 마지막 검색 결과 투표 ID (첫 페이지 조회 시 입력 X)"),
                            parameterWithName("cursorLikes").optional().description("이전 마지막 검색 결과 좋아요 개수 (첫 페이지 조회 시 입력 X)"),
                            parameterWithName("size").optional().description("검색할 ROW 수"),
                            parameterWithName("searchType").description("검색 타입 (BEST, HOME)")
                    ),
                    responseFields(
                            fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부 "),
                            subsectionWithPath("votes").type(JsonFieldType.ARRAY).description("투표 목록")
                    )
            )).andDo(restDocs.document(
                            responseFields(beneathPath("votes").withSubsectionId("votes"),
                                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("투표 ID"),
                                    fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("투표 작성자 ID"),
                                    fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("투표 이미지 URL"),
                                    fieldWithPath("nickName").type(JsonFieldType.STRING).description("투표 작성자 닉네임"),
                                    fieldWithPath("memberImageUrl").type(JsonFieldType.STRING).description("투표 작성자 이미지 URL"),
                                    fieldWithPath("commentCount").type(JsonFieldType.NUMBER).description("댓글 개수"),
                                    fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("좋아요 개수"),
                                    fieldWithPath("disLikeCount").type(JsonFieldType.NUMBER).description("싫어요 개수"),
                                    fieldWithPath("totalEvaluationCnt").type(JsonFieldType.NUMBER).description("총 투표 개수"),
                                    fieldWithPath("likeRatio").type(JsonFieldType.NUMBER).description("총 투표 개수"),
                                    fieldWithPath("disLikeRatio").type(JsonFieldType.NUMBER).description("총 투표 개수"),
                                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("투표 생성일"),
                                    fieldWithPath("bookmarked").type(JsonFieldType.BOOLEAN).description("내가 북마크 했는지 여부 (true, false) "),
                                    fieldWithPath("status").type(JsonFieldType.STRING).description("해당 투표에 대한 나의 상태 (NONE, LIKE, DISLIKE)")
                            )
                    )
            );
        }

        @Test
        void 미인증_사용자일_경우_401_응답() throws Exception {

            // when & then
            mockMvc.perform(get(BASE_URL)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }


}
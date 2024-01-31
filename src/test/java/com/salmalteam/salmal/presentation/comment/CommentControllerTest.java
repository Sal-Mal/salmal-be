package com.salmalteam.salmal.presentation.comment;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.salmalteam.salmal.comment.dto.request.CommentReplyCreateRequest;
import com.salmalteam.salmal.comment.dto.response.ReplayCommentDto;
import com.salmalteam.salmal.comment.dto.response.ReplyPageResponse;
import com.salmalteam.salmal.comment.dto.response.ReplyResponse;
import com.salmalteam.salmal.support.PresentationTest;
import com.salmalteam.salmal.vote.dto.request.VoteCommentUpdateRequest;

class CommentControllerTest extends PresentationTest {

    private static final String BASE_URL = "/api/comments/{comment-id}";


    @Nested
    class 댓글_수정_테스트 {
        @Test
        void 댓글_수정_성공() throws Exception {
            // given
            final Long commentId = 1L;
            final String content = "수정할 댓글입니다";
            final VoteCommentUpdateRequest voteCommentUpdateRequest = new VoteCommentUpdateRequest(content);
            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.put(BASE_URL, commentId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .content(createJson(voteCommentUpdateRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    pathParameters(
                            parameterWithName("comment-id").description("수정할 댓글 ID")
                    ),
                    requestFields(
                            fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 댓글 내용")
                    )
            ));

            verify(commentService, times(1)).updateComment(any(), any(), any());

        }

        @Test
        void 미인증_사용자일_경우_401_응답() throws Exception {

            // given
            final Long commentId = 1L;

            // when & then
            mockMvc.perform(RestDocumentationRequestBuilders.put(BASE_URL, commentId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void 수정할_댓글이_빈칸인_경우_400_응답() throws Exception {

            // given
            final Long commentId = 1L;
            final String content = "";
            final VoteCommentUpdateRequest voteCommentUpdateRequest = new VoteCommentUpdateRequest(content);
            mockingForAuthorization();

            // when & then
            mockMvc.perform(RestDocumentationRequestBuilders.put(BASE_URL, commentId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .content(createJson(voteCommentUpdateRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class 댓글_삭제_테스트{
        @Test
        void 댓글_삭제_성공() throws Exception{
            // given
            final Long commentId = 1L;

            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.delete(BASE_URL, commentId)
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
                            parameterWithName("comment-id").description("삭제할 댓글 ID")
                    )
            ));

            verify(commentService).deleteComment(any(), any());

        }
    }

    @Nested
    class 댓글_좋아요_테스트{

        private static final String URL = "/likes";
        @Test
        void 댓글_좋아요_성공() throws Exception{

            // given
            final Long commentId = 1L;
            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + URL, commentId)
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
                            parameterWithName("comment-id").description("좋아요할 댓글 ID")
                    )
            ));

            verify(commentService, times(1)).likeComment(any(), any());
        }
    }

    @Nested
    class 댓글_좋아요_취소_테스트{

        private static final String URL = "/likes";
        @Test
        void 댓글_좋아요_취소_성공() throws Exception{
            // given
            final Long commentId = 1L;
            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.delete(BASE_URL +URL, commentId)
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
                            parameterWithName("comment-id").description("좋아요 취소할 댓글 ID")
                    )
            ));

            verify(commentService, times(1)).unLikeComment(any(), any());
        }

    }

    @Nested
    class 댓글_신고_테스트{
        private static final String URL = "/reports";

        @Test
        void 댓글_신고_성공() throws Exception{
            // given
            final Long commentId = 1L;

            mockingForAuthorization();
            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL +URL, commentId)
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
                            parameterWithName("comment-id").description("신고할 댓글 ID")
                    )
            ));

            verify(commentService, times(1)).report(any(), any());

        }

        @Test
        void 미인증_사용자일_경우_401_응답() throws Exception {

            // given
            final Long commentId = 1L;

            // when & then
            mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + URL, commentId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class 대댓글_생성_테스트{
        private final String URL = "/replies";

        @Test
        void 대댓글_생성_성공() throws Exception{
            // given
            final Long commentId = 1L;
            final String content = "이 댓글에 동의합니다!";
            final CommentReplyCreateRequest commentReplyCreateRequest = new CommentReplyCreateRequest(content);
            ReplayCommentDto replayCommentDto = new ReplayCommentDto(100L, 20L, 556L, "kim", "content","url","url");
            given(commentService.replyComment(any(),anyLong(),any()))
                .willReturn(replayCommentDto);
            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL + URL, commentId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .content(createJson(commentReplyCreateRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isCreated());

            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    pathParameters(
                            parameterWithName("comment-id").description("대댓글을 추가할 댓글 ID")
                    ),
                    requestFields(
                            fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용 ( 최소 1글자 ~ 최대 100글자 ) ")
                    )
            ));

            verify(commentService, times(1)).replyComment(any(), any(), any());
            verify(notificationService, times(1)).save(anyLong(),anyLong(),anyString(),anyString(),anyLong());
            verify(fcmClient, times(1)).pub(any());

        }
    }

    @Nested
    class 대댓글_목록_조회_테스트{

        private final String URL = "/replies";
        @Test
        void 대댓글_목록_조회_성공() throws Exception{
            // given
            final Long commentId = 1L;
            final Long cursorId = 10L;
            final Integer size = 10;

            final ReplyResponse replyResponse1 = new ReplyResponse(11L, 15L, "느티나무", "imageUrl", true, 10, "인정합니다!", LocalDateTime.now(), LocalDateTime.now());
            final ReplyResponse replyResponse2 = new ReplyResponse(12L, 22L, "소나무", "imageUrl", false, 13, "인정합니다!", LocalDateTime.now(), LocalDateTime.now());
            final ReplyResponse replyResponse3 = new ReplyResponse(13L, 53L, "버드나무", "imageUrl", true, 20, "인정합니다!", LocalDateTime.now(), LocalDateTime.now());

            final ReplyPageResponse replyPageResponse = ReplyPageResponse.of(true, List.of(replyResponse1, replyResponse2, replyResponse3));

            given(commentService.searchReplies(any(), any(), any())).willReturn(replyPageResponse);
            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + URL, commentId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN)
                            .param("cursorId", String.valueOf(cursorId))
                            .param("size", String.valueOf(size))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            // then
            resultActions.andDo(restDocs.document(
                    requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 타입 AccessToken")
                    ),
                    pathParameters(
                            parameterWithName("comment-id").description("대댓글 목록을 조회할 댓글 ID")
                    ),
                    requestParameters(
                            parameterWithName("cursorId").optional().description("이전 마지막 검색 결과 대댓글 ID (첫 페이지 조회 시 입력 X)"),
                            parameterWithName("size").optional().description("검색할 ROW 수")
                    ),
                    responseFields(
                            fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부"),
                            subsectionWithPath("comments").type(JsonFieldType.ARRAY).description("대댓글 목록")
                    )
            )).andDo(restDocs.document(
                            responseFields(beneathPath("comments").withSubsectionId("comments"),
                                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("대댓글 ID"),
                                    fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("대댓글 작성자 ID"),
                                    fieldWithPath("nickName").type(JsonFieldType.STRING).description("대댓글 작성자 닉네임"),
                                    fieldWithPath("memberImageUrl").type(JsonFieldType.STRING).description("대댓글 작성자 이미지 URL"),
                                    fieldWithPath("liked").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
                                    fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("좋아요 개수"),
                                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("대댓글 생성일"),
                                    fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("대댓글 수정일")
                            )
                    )
            );
        }

    }

    @Nested
    class 대댓글_목록_전체_조회_테스트{

        private final String URL = "/replies/all";
        @Test
        void 대댓글_목록_전체_조회_테스트_성공() throws Exception{

            final Long commentId = 1L;

            final ReplyResponse replyResponse1 = new ReplyResponse(11L, 15L, "느티나무", "imageUrl", true, 10, "인정합니다!", LocalDateTime.now(), LocalDateTime.now());
            final ReplyResponse replyResponse2 = new ReplyResponse(12L, 22L, "소나무", "imageUrl", false, 13, "인정합니다!", LocalDateTime.now(), LocalDateTime.now());
            final ReplyResponse replyResponse3 = new ReplyResponse(13L, 53L, "버드나무", "imageUrl", true, 20, "인정합니다!", LocalDateTime.now(), LocalDateTime.now());

            given(commentService.searchAllReplies( any(), any())).willReturn(List.of(replyResponse1, replyResponse2, replyResponse3));
            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(BASE_URL + URL, commentId)
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
                            parameterWithName("comment-id").description("대댓글 전체 목록을 조회할 댓글 ID")
                    ),
                    responseFields(
                            subsectionWithPath("[].id").type(JsonFieldType.NUMBER).description("대댓글 ID"),
                            subsectionWithPath("[].memberId").type(JsonFieldType.NUMBER).description("대댓글 작성자 ID"),
                            subsectionWithPath("[].nickName").type(JsonFieldType.STRING).description("대댓글 작성자 닉네임"),
                            subsectionWithPath("[].memberImageUrl").type(JsonFieldType.STRING).description("대댓글 작성자 이미지 URL"),
                            subsectionWithPath("[].liked").type(JsonFieldType.BOOLEAN).description("좋아요 여부"),
                            subsectionWithPath("[].likeCount").type(JsonFieldType.NUMBER).description("좋아요 개수"),
                            subsectionWithPath("[].content").type(JsonFieldType.STRING).description("내용"),
                            subsectionWithPath("[].createdAt").type(JsonFieldType.STRING).description("대댓글 생성일"),
                            subsectionWithPath("[].updatedAt").type(JsonFieldType.STRING).description("대댓글 수정일")
                    )
            ));
        }

    }


}
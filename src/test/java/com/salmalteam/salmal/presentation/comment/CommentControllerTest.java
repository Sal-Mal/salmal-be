package com.salmalteam.salmal.presentation.comment;

import com.salmalteam.salmal.dto.request.vote.VoteBookmarkRequest;
import com.salmalteam.salmal.dto.request.vote.VoteCommentUpdateRequest;
import com.salmalteam.salmal.dto.response.comment.CommentPageResponse;
import com.salmalteam.salmal.dto.response.comment.CommentResponse;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}
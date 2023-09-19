package com.salmalteam.salmal.presentation.vote;

import com.salmalteam.salmal.domain.vote.evaluation.VoteEvaluationType;
import com.salmalteam.salmal.dto.request.vote.VoteBookmarkRequest;
import com.salmalteam.salmal.dto.request.vote.VoteEvaluateRequest;
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
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends PresentationTest {

    private static final String BASE_URL = "/api/votes";

    @Nested
    class 투표_등록_테스트 {
        @Test
        void 투표_등록_성공() throws Exception{
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
        void 미인증_사용자일_경우_401_응답() throws Exception{

            // when & then
            mockMvc.perform(multipart(BASE_URL)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void 이미지_파일이_없는_경우_400_응답() throws Exception{

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
    class 투표_평가_테스트{

        private final static String URL = "/{vote-id}/evaluations";
        @Test
        void 투표_평가_성공() throws Exception{
            // given
            final Long voteId = 1L;
            final String voteEvaluationType = "LIKE";
            final VoteEvaluateRequest voteEvaluateRequest = new VoteEvaluateRequest(voteEvaluationType);

            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL+URL, voteId)
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
        void 미인증_사용자일_경우_401_응답() throws Exception{

            // given
            final Long voteId = 1L;

            // when & then
            mockMvc.perform(post(BASE_URL + URL, voteId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @CsvSource(value = {"likey","lik", "dis", "disLik"})
        void 유효한_평가_타입이_아닌_경우_400_응답(final String voteEvaluationType) throws Exception{

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
    class 투표_북마크_테스트{

        private final static String URL = "/{vote-id}/bookmarks";
        @Test
        void 투표_북마킹_성공() throws Exception{
            // given
            final Long voteId = 1L;
            final Boolean isBookmarked = true;
            final VoteBookmarkRequest voteBookmarkRequest = new VoteBookmarkRequest(isBookmarked);
            mockingForAuthorization();

            // when
            final ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post(BASE_URL+URL, voteId)
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

            verify(voteService, times(1)).bookmark(any(), any(),any());

        }

        @Test
        void 미인증_사용자일_경우_401_응답() throws Exception{

            // given
            final Long voteId = 1L;

            // when & then
            mockMvc.perform(post(BASE_URL + URL, voteId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void 북마크_여부가_전달이_안된_경우_400_응답() throws Exception{

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


}
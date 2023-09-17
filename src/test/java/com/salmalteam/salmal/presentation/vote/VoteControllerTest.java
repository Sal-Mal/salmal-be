package com.salmalteam.salmal.presentation.vote;

import com.salmalteam.salmal.support.PresentationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
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
}
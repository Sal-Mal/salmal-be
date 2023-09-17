package com.salmalteam.salmal.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salmalteam.salmal.application.auth.AuthService;
import com.salmalteam.salmal.application.auth.TokenExtractor;
import com.salmalteam.salmal.application.auth.TokenProvider;
import com.salmalteam.salmal.application.vote.VoteService;
import com.salmalteam.salmal.config.RestDocsConfig;
import com.salmalteam.salmal.domain.auth.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfig.class)
@WebMvcTest
public class PresentationTest {

    protected final String ACCESS_TOKEN = "Bearer accessToken";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected VoteService voteService;

    @MockBean
    protected TokenProvider tokenProvider;

    @MockBean
    protected TokenExtractor tokenExtractor;

    @MockBean
    protected TokenRepository tokenRepository;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation, WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(MockMvcResultHandlers.print())
                .alwaysDo(restDocs)
                .build();
    }

    protected String createJson(Object dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    protected void mockingForAuthorization(){
        given(tokenProvider.isValidAccessToken(any())).willReturn(true);
        given(tokenRepository.existsLogoutAccessTokenById(any())).willReturn(false);
    }
}

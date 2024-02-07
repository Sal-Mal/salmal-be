package com.salmalteam.salmal.support;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;

import java.util.Optional;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salmalteam.salmal.auth.application.AuthPayloadGenerator;
import com.salmalteam.salmal.auth.application.AuthService;
import com.salmalteam.salmal.auth.application.TokenExtractor;
import com.salmalteam.salmal.auth.entity.AuthPayload;
import com.salmalteam.salmal.auth.entity.Role;
import com.salmalteam.salmal.auth.entity.TokenRepository;
import com.salmalteam.salmal.auth.infrastructure.JwtProvider;
import com.salmalteam.salmal.auth.infrastructure.RefreshTokenProvider;
import com.salmalteam.salmal.comment.application.CommentService;
import com.salmalteam.salmal.config.RestDocsConfig;
import com.salmalteam.salmal.fcm.infra.FcmClient;
import com.salmalteam.salmal.member.application.MemberService;
import com.salmalteam.salmal.notification.service.MemberNotificationService;
import com.salmalteam.salmal.notification.service.NotificationService;
import com.salmalteam.salmal.presentation.http.auth.AuthenticationContext;
import com.salmalteam.salmal.vote.application.VoteService;

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
	protected MemberService memberService;

	@MockBean
	protected VoteService voteService;

	@MockBean
	protected CommentService commentService;

	@MockBean
	protected JwtProvider jwtProvider;

	@MockBean
	protected TokenExtractor tokenExtractor;

    @MockBean
    protected TokenRepository tokenRepository;

	@MockBean
	protected RefreshTokenProvider refreshTokenProvider;

	@MockBean
	protected AuthenticationContext authenticationContext;

	@MockBean
	protected AuthPayloadGenerator authPayloadGenerator;

	@MockBean
	protected NotificationService notificationService;
	@MockBean
	protected MemberNotificationService memberNotificationService;
	@MockBean
	protected FcmClient fcmClient;

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
		given(tokenRepository.existsLogoutAccessTokenById(any())).willReturn(false);
		given(authPayloadGenerator.generateByToken(any())).willReturn(AuthPayload.of(100L, Role.MEMBER));
		given(authenticationContext.getId()).willReturn(100L);
		given(authenticationContext.getRole()).willReturn(Role.MEMBER);
		given(tokenExtractor.extractByHeader(any())).willReturn(Optional.of("accessToken"));

	}
}

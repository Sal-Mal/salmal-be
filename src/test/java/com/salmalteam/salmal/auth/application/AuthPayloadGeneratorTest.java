package com.salmalteam.salmal.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.salmalteam.salmal.auth.entity.MemberPayLoad;
import com.salmalteam.salmal.auth.entity.Role;
import com.salmalteam.salmal.auth.entity.TokenRepository;
import com.salmalteam.salmal.auth.exception.AuthException;

import io.jsonwebtoken.security.Keys;

@ExtendWith(MockitoExtension.class)
class AuthPayloadGeneratorTest {

	AuthPayloadGenerator authPayloadGenerator;
	@Mock
	TokenRepository tokenRepository;

	FixedTokenProvider fixedTokenProvider;

	@BeforeEach
	void setUp() {
		String secret = "testSecKeytestSecKeytestSecKey123";
		SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		authPayloadGenerator = new AuthPayloadGenerator(tokenRepository, "testSecKeytestSecKeytestSecKey123");
		fixedTokenProvider = new FixedTokenProvider(100000000L, secretKey);
	}

	@Test
	@DisplayName("token 의 정보로 MemberPayLoad 를 생성한다.")
	void generate() {
		//given
		given(tokenRepository.existsLogoutAccessTokenById(anyString())).willReturn(false);
		String refreshToken = fixedTokenProvider.provide(createPayloadWithIdClaim(100L));

		//when
		MemberPayLoad memberPayLoad = authPayloadGenerator.generateByToken(refreshToken);

		//then
		assertThat(memberPayLoad.getId()).isEqualTo(100L);
		assertThat(memberPayLoad.getRole()).isEqualTo(Role.MEMBER);
		then(tokenRepository).should(times(1)).existsRefreshTokenById(anyString());

	}

	@Test
	@DisplayName("token 이 로그아웃 되었으면 예외가 발생한다.")
	void generate_logout_token() {
		//given
		given(tokenRepository.existsLogoutAccessTokenById(anyString())).willReturn(true);
		String refreshToken = fixedTokenProvider.provide(createPayloadWithIdClaim(100L));

		//expect
		assertThatThrownBy(() -> authPayloadGenerator.generateByToken(refreshToken))
			.isInstanceOf(AuthException.class)
			.hasMessage("로그아웃된 접근 토큰 요청 -> [ type = UNAUTHORIZED ] [ code = 4005 ] ");

		then(tokenRepository).should(times(1)).existsRefreshTokenById(anyString());
	}


	//TODO
	@Test
	@DisplayName("만료된 토큰이면 예외가 발생한다.")
	void generate_expired_token() throws Exception {
	    //given

	    //when

	    //then
	}

	@Test
	@DisplayName("JWT 토큰 타입이 아니면 예외가 발생한다.")
	void generate_invalid_jwt_type() throws Exception {
	    //given

	    //when

	    //then

	}

	@Test
	@DisplayName("페이로드에 id 와 role 이 존재하지 않으면 예외가 발생한다.")
	void generate_payload_does_not_exist() throws Exception {
	    //given

	    //when

	    //then

	}

	private Map<String, Object> createPayloadWithIdClaim(Long id) {
		HashMap<String, Object> payload = new HashMap<>();
		payload.put("id", id);
		return payload;
	}
}
package com.salmalteam.salmal.presentation.http.auth;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.salmalteam.salmal.auth.annotation.LoginMember;
import com.salmalteam.salmal.auth.application.AuthService;
import com.salmalteam.salmal.auth.dto.request.LoginRequest;
import com.salmalteam.salmal.auth.dto.request.LogoutRequest;
import com.salmalteam.salmal.auth.dto.request.ReissueTokenRequest;
import com.salmalteam.salmal.auth.dto.request.SignUpRequest;
import com.salmalteam.salmal.auth.dto.response.LoginResponse;
import com.salmalteam.salmal.auth.dto.response.TokenAvailableResponse;
import com.salmalteam.salmal.auth.dto.response.TokenResponse;
import com.salmalteam.salmal.member.application.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

	private final static String AUTHORIZATION_HEADER = "Authorization";
	private final AuthService authService;
	private final MemberService memberService;

	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public LoginResponse login(@RequestBody final LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}

	@PostMapping("/signup/{provider}")
	@ResponseStatus(HttpStatus.OK)
	public LoginResponse signUp(@PathVariable final String provider,
		@Valid @RequestBody final SignUpRequest signUpRequest) {
		return authService.signUp(provider, signUpRequest);
	}

	@PostMapping("/reissue")
	@ResponseStatus(HttpStatus.OK)
	public TokenResponse reissue(@LoginMember Long memberId,
		@RequestBody final ReissueTokenRequest reissueTokenRequest) {
		final String refreshToken = reissueTokenRequest.getRefreshToken();
		return authService.reissueAccessToken(memberId, refreshToken);
	}

	@PostMapping("/logout")
	@ResponseStatus(HttpStatus.OK)
	public void logout(@RequestHeader(value = AUTHORIZATION_HEADER) final String authHeader,
		@RequestBody final LogoutRequest logoutRequest) {
		final String accessToken = extractAccessTokenFromAuthHeader(authHeader);
		authService.logout(accessToken, logoutRequest);
	}

	@GetMapping("/tokens")
	public TokenAvailableResponse validateToken(@LoginMember final Long memberId) {
		return new TokenAvailableResponse(memberService.isActivatedId(memberId));
	}

	private String extractAccessTokenFromAuthHeader(final String authHeader) {
		final String accessToken = authHeader.substring(7);
		return accessToken;
	}
}

package com.salmalteam.salmal.presentation.auth;

import com.salmalteam.salmal.application.auth.AuthService;
import com.salmalteam.salmal.dto.request.auth.LoginRequest;
import com.salmalteam.salmal.dto.request.auth.LogoutRequest;
import com.salmalteam.salmal.dto.request.auth.ReissueTokenRequest;
import com.salmalteam.salmal.dto.request.auth.SignUpRequest;
import com.salmalteam.salmal.dto.response.auth.LoginResponse;
import com.salmalteam.salmal.dto.response.auth.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody final LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/signup/{provider}")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse signUp(@PathVariable final String provider,@Valid @RequestBody final SignUpRequest signUpRequest){
        return authService.signUp(provider, signUpRequest);
    }

    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse reissue(@RequestBody final ReissueTokenRequest reissueTokenRequest){
        final String refreshToken = reissueTokenRequest.getRefreshToken();
        return authService.reissueAccessToken(refreshToken);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@RequestHeader(value = AUTHORIZATION_HEADER) final String authHeader, @RequestBody final LogoutRequest logoutRequest){
        final String accessToken = extractAccessTokenFromAuthHeader(authHeader);
        authService.logout(accessToken, logoutRequest);
    }

    private String extractAccessTokenFromAuthHeader(final String authHeader){
        final String accessToken = authHeader.substring(7);
        return accessToken;
    }

}

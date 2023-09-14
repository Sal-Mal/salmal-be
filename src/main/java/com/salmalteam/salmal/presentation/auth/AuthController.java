package com.salmalteam.salmal.presentation.auth;

import com.salmalteam.salmal.application.auth.AuthService;
import com.salmalteam.salmal.dto.request.LoginRequest;
import com.salmalteam.salmal.dto.request.SignUpRequest;
import com.salmalteam.salmal.dto.response.LoginResponse;
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
}

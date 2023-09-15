package com.salmalteam.salmal.dto.request.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpRequest {

    @NotBlank(message = "소셜 로그인 ID(providerId)를 입력해주세요")
    private String providerId;
    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickName;
    @NotNull(message = "마케팅 수신 이용약관 동의여부의 결과값이 비어있습니다.")
    private Boolean marketingInformationConsent;

    public SignUpRequest(final String providerId, final String nickName, final Boolean marketingInformationConsent){
        this.providerId = providerId;
        this.nickName = nickName;
        this.marketingInformationConsent = marketingInformationConsent;
    }
}

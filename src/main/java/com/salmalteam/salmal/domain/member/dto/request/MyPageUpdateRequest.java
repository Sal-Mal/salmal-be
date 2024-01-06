package com.salmalteam.salmal.domain.member.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class MyPageUpdateRequest {

    @NotBlank(message = "수정할 닉네임을 입력해주세요.")
    private final String nickName;
    @NotBlank(message = "수정할 한줄 소개글을 입력해주세요.")
    private final String introduction;
    public MyPageUpdateRequest(String nickName, String introduction) {
        this.nickName = nickName;
        this.introduction = introduction;
    }
}

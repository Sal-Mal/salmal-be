package com.salmalteam.salmal.member.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
public class MemberImageUpdateRequest {

    @NotNull(message = "업로드할 파일(이미지)이 존재하지 않습니다.")
    private final MultipartFile imageFile;
    public MemberImageUpdateRequest(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}

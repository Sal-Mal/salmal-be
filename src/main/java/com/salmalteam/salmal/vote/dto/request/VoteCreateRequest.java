package com.salmalteam.salmal.vote.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
public class VoteCreateRequest {

    @NotNull(message = "업로드할 파일(이미지)이 존재하지 않습니다.")
    private final MultipartFile imageFile;
    public VoteCreateRequest(final MultipartFile imageFile){
        this.imageFile = imageFile;
    }
}

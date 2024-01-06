package com.salmalteam.salmal.domain.vote.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VoteCommentCreateRequest {

    @NotBlank(message = "댓글을 입력해주세요.")
    private String content;

    public VoteCommentCreateRequest(final String content){
        this.content = content;
    }
}

package com.salmalteam.salmal.dto.request.vote;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VoteCommentUpdateRequest {

    @NotBlank(message = "변경할 댓글을 입력해주세요.")
    private String content;

    public VoteCommentUpdateRequest(final String content){
        this.content = content;
    }
}

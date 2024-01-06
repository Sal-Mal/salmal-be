package com.salmalteam.salmal.domain.comment.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentReplyCreateRequest {

    @NotBlank(message = "댓글을 입력해주세요")
    private String content;

    public CommentReplyCreateRequest(final String content) {
        this.content = content;
    }
}

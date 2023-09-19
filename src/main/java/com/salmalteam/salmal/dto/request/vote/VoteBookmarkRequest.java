package com.salmalteam.salmal.dto.request.vote;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VoteBookmarkRequest {
    @NotNull(message = "북마크 여부를 보내주세요(true, false)")
    private Boolean isBookmarked;

    public VoteBookmarkRequest(final Boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }
}

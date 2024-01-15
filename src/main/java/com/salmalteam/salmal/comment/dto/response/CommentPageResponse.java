package com.salmalteam.salmal.comment.dto.response;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.function.Predicate;

import lombok.Getter;

@Getter
public class CommentPageResponse {
    private boolean hasNext;
    private List<CommentResponse> comments;
    private CommentPageResponse(boolean hasNext, List<CommentResponse> comments) {
        this.hasNext = hasNext;
        this.comments = comments;
    }

    public static CommentPageResponse of(boolean hasNext, List<CommentResponse> comments){
        return new CommentPageResponse(hasNext, comments);
    }

    public void filteringBlockedMembers(List<Long> ids) {
        comments = comments.stream()
            .filter(filterBlockedMemberPredicate(ids))
            .collect(toList());
    }

    private Predicate<CommentResponse> filterBlockedMemberPredicate(List<Long> ids) {
        return voteResponse -> ids.stream()
            .noneMatch(id -> id.equals(voteResponse.getMemberId()));
    }
}

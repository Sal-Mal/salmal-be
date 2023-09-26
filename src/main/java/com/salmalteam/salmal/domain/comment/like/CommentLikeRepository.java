package com.salmalteam.salmal.domain.comment.like;

import com.salmalteam.salmal.domain.comment.Comment;
import com.salmalteam.salmal.domain.member.Member;
import org.springframework.data.repository.Repository;

public interface CommentLikeRepository extends Repository<CommentLike, Long> {

    CommentLike save(CommentLike commentLike);

    boolean existsByCommentAndLiker(Comment comment, Member member);
    void deleteByCommentAndLiker(Comment comment, Member member);
}

package com.salmalteam.salmal.domain.comment.like;

import com.salmalteam.salmal.domain.comment.Comment;
import com.salmalteam.salmal.domain.member.Member;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface CommentLikeRepository extends Repository<CommentLike, Long> {

    CommentLike save(CommentLike commentLike);
    List<CommentLike> findAllByLiker_Id(Long memberId);
    boolean existsByCommentAndLiker(Comment comment, Member member);
    void deleteByCommentAndLiker(Comment comment, Member member);
}

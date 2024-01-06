package com.salmalteam.salmal.comment.entity.like;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.salmalteam.salmal.comment.entity.Comment;
import com.salmalteam.salmal.member.entity.Member;

public interface CommentLikeRepository extends Repository<CommentLike, Long> {

    CommentLike save(CommentLike commentLike);
    List<CommentLike> findAllByLiker_Id(Long memberId);
    boolean existsByCommentAndLiker(Comment comment, Member member);
    void deleteByCommentAndLiker(Comment comment, Member member);
}

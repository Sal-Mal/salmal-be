package com.salmalteam.salmal.domain.comment;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CommentRepository extends Repository<Comment, Long>, CommentRepositoryCustom {
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update Comment c set c.likeCount = c.likeCount + 1 where c.id = :id")
    void increaseLikeCount(Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update Comment c set c.likeCount = c.likeCount - 1 where c.id = :id")
    void decreaseLikeCount(Long id);
}

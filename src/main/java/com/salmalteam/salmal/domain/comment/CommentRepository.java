package com.salmalteam.salmal.domain.comment;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CommentRepository extends Repository<Comment, Long>, CommentRepositoryCustom {
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
}

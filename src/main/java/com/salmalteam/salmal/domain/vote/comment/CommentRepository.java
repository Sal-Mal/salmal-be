package com.salmalteam.salmal.domain.vote.comment;

import org.springframework.data.repository.Repository;

public interface CommentRepository extends Repository<Comment, Long>, CommentRepositoryCustom {
    Comment save(Comment comment);
}

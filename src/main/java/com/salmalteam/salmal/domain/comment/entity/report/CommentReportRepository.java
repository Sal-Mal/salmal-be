package com.salmalteam.salmal.domain.comment.entity.report;

import com.salmalteam.salmal.domain.comment.entity.Comment;
import com.salmalteam.salmal.domain.member.entity.Member;
import org.springframework.data.repository.Repository;

public interface CommentReportRepository extends Repository<CommentReport, Long> {
    void save(CommentReport commentReport);
    boolean existsByCommentAndReporter(Comment comment, Member member);
}

package com.salmalteam.salmal.domain.comment.report;

import com.salmalteam.salmal.domain.comment.Comment;
import com.salmalteam.salmal.domain.member.Member;
import org.springframework.data.repository.Repository;

public interface CommentReportRepository extends Repository<CommentReport, Long> {
    void save(CommentReport commentReport);
    boolean existsByCommentAndReporter(Comment comment, Member member);
}

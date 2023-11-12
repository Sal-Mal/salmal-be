package com.salmalteam.salmal.domain.comment;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends Repository<Comment, Long>, CommentRepositoryCustom {
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
    boolean existsById(Long id);
    List<Comment> findAllByVote_Id(Long voteId);
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE from Comment c where c.id in :commentIdsToDel")
    void deleteAllCommentsByIdIn(@Param("commentIdsToDel") List<Long> commentIdsToDel);
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE from Comment c where c.parentComment.id in :commentIdsToDel")
    void deleteAllRepliesByIdIn(@Param("commentIdsToDel") List<Long> commentIdsToDel);
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE from Comment c where c.commenter.id = :commenterId AND c.commentType = :commentType")
    void deleteAllCommentsByCommenterId(@Param("commenterId") Long commenterId, @Param("commentType") CommentType commentType);
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update Comment c set c.likeCount = c.likeCount + 1 where c.id = :id")
    void increaseLikeCount(Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update Comment c set c.likeCount = c.likeCount - 1 where c.id = :id")
    void decreaseLikeCount(Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update Comment c set c.replyCount = c.replyCount + 1 where c.id = :id")
    void increaseReplyCount(Long id);
}

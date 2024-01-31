package com.salmalteam.salmal.comment.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends Repository<Comment, Long>, CommentRepositoryCustom {
	Comment save(Comment comment);
	void delete(Comment comment);
	@Query("select c from Comment c join fetch c.commenter join fetch c.vote where c.id =:id")
	Optional<Comment> findById(@Param("id") Long id);
	boolean existsById(Long id);
	List<Comment> findAllByCommenter_Id(Long commenterId);
	List<Comment> findALlByCommenter_idAndCommentType(Long commenterId, CommentType commentType);
	List<Comment> findAllByVote_Id(Long voteId);
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "update Comment c set c.replyCount = c.replyCount - 1 where c.id = :id")
	void decreaseReplyCount(Long id);
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE from Comment c where c.parentComment.id = :commentId")
	void deleteAllRepliesByParentCommentId(@Param("commentId") Long commentId);
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

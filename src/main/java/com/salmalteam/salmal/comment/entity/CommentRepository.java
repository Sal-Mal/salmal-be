package com.salmalteam.salmal.comment.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

	@Query("select c from Comment c join fetch c.commenter where c.id =:id")
	Optional<Comment> findByIdFetchJoinCommenter(@Param("id") Long id);

	boolean existsById(Long id);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "update Comment c set c.replyCount = c.replyCount - 1 where c.id = :id")
	void decreaseReplyCount(Long id);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE from Comment c where c.parentComment.id = :commentId")
	void deleteAllRepliesByParentCommentId(@Param("commentId") Long commentId);

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

package com.salmalteam.salmal.vote.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VoteRepository extends JpaRepository<Vote, Long>, VoteRepositoryCustom {
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("DELETE from Vote v where v.id in :voteIdsToDel")
	void deleteAllByIdIn(@Param("voteIdsToDel") List<Long> voteIdsToDel);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "update Vote v set v.commentCount = v.commentCount + 1 where v.id = :id")
	void increaseCommentCount(Long id);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "update Vote v set v.commentCount = v.commentCount - 1 where v.id = :id")
	void decreaseCommentCount(Long id);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "update Vote v " +
		"set v.likeRatio = ( v.likeCount + 1 ) / cast((v.evaluationCount + 1) as double)," +
		"v.dislikeRatio = v.dislikeCount / cast((v.evaluationCount + 1) as double)," +
		"v.likeCount = v.likeCount + 1," +
		"v.evaluationCount = v.evaluationCount + 1 where v.id = :id")
	void updateVoteEvaluationStatisticsForEvaluationLikeInsert(Long id);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "update Vote v " +
		"set v.likeRatio = v.likeCount / cast((v.evaluationCount + 1) as double)," +
		"v.dislikeRatio = ( v.dislikeCount + 1 ) / cast((v.evaluationCount + 1) as double)," +
		"v.dislikeCount = v.dislikeCount + 1," +
		"v.evaluationCount = v.evaluationCount + 1 where v.id = :id")
	void updateVoteEvaluationStatisticsForEvaluationDisLikeInsert(Long id);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "update Vote v " +
		"set v.likeRatio = case v.evaluationCount when 1 then 0 else (( v.likeCount - 1 ) / cast(v.evaluationCount - 1 as double)) end , "
		+
		"v.dislikeRatio = case v.evaluationCount when 1 then 0 else (v.dislikeCount / cast((v.evaluationCount - 1) as double)) end , "
		+
		"v.likeCount = v.likeCount - 1, " +
		"v.evaluationCount = v.evaluationCount - 1 " +
		"where v.id = :id")
	void updateVoteEvaluationsStatisticsForEvaluationLikeDelete(Long id);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "update Vote v " +
		"set v.likeRatio = case v.evaluationCount when 1 then 0 else ( v.likeCount / cast(v.evaluationCount - 1 as double)) end , "
		+
		"v.dislikeRatio = case v.evaluationCount when 1 then 0 else ( ( v.dislikeCount - 1 ) / cast((v.evaluationCount - 1) as double))end , "
		+
		"v.dislikeCount = v.dislikeCount - 1, " +
		"v.evaluationCount = v.evaluationCount - 1 " +
		"where v.id = :id")
	void updateVoteEvaluationsStatisticsForEvaluationDisLikeDelete(Long id);
}

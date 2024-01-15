package com.salmalteam.salmal.member.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberBlockedRepository extends JpaRepository<MemberBlocked, Long>, MemberBlockedRepositoryCustom {
	MemberBlocked save(MemberBlocked memberBlocked);

	void deleteByBlockerAndTarget(Member blocker, Member target);

	boolean existsByBlockerAndTarget(Member blocker, Member target);

	@Query("select mb.target.id from MemberBlocked mb where mb.blocker.id =:blockerId")
	List<Long> findTargetMemberIdByMemberId(@Param("blockerId") Long blockerId);
}
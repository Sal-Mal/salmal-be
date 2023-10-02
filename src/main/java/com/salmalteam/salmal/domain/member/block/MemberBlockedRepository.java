package com.salmalteam.salmal.domain.member.block;

import com.salmalteam.salmal.domain.member.Member;
import org.springframework.data.repository.Repository;

public interface MemberBlockedRepository extends Repository<MemberBlocked, Long>, MemberBlockedRepositoryCustom {
    MemberBlocked save(MemberBlocked memberBlocked);
    void deleteByBlockerAndTarget(Member blocker, Member target);
    boolean existsByBlockerAndTarget(Member blocker, Member target);
}

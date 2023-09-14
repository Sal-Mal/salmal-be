package com.salmalteam.salmal.domain.member;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MemberRepository extends Repository <Member, Long>{
    Member save(Member member);
    Optional<Member> findByProviderId(String providerId);
    boolean existsByNickName(NickName nickName);
}

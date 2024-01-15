package com.salmalteam.salmal.member.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository <Member, Long>, MemberRepositoryCustom{
    Optional<Member> findByProviderId(String providerId);
    boolean existsByNickName(NickName nickName);
    boolean existsById(Long memberId);
}

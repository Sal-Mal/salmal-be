package com.salmalteam.salmal.member.entity;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MemberRepository extends Repository <Member, Long>, MemberRepositoryCustom{
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByProviderId(String providerId);
    boolean existsByNickName(NickName nickName);
    boolean existsById(Long memberId);
    void delete(Member member);
}

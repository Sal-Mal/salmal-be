package com.salmalteam.salmal.config;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.salmalteam.salmal.comment.entity.CommentRepositoryCustomImpl;
import com.salmalteam.salmal.member.entity.MemberBlockedRepositoryCustomImpl;
import com.salmalteam.salmal.member.entity.MemberRepositoryCustomImpl;
import com.salmalteam.salmal.vote.entity.VoteRepositoryCustomImpl;

@TestConfiguration
@EnableJpaAuditing
public class DataJpaTestConfig {
	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	public JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(entityManager);
	}

	@Bean
	public MemberBlockedRepositoryCustomImpl memberBlockedRepositoryCustom() {
		return new MemberBlockedRepositoryCustomImpl(jpaQueryFactory());
	}

	@Bean
	public CommentRepositoryCustomImpl commentRepositoryCustom() {
		return new CommentRepositoryCustomImpl(jpaQueryFactory());
	}

	@Bean
	public MemberRepositoryCustomImpl memberRepositoryCustom() {
		return new MemberRepositoryCustomImpl(jpaQueryFactory());
	}

	@Bean
	public VoteRepositoryCustomImpl voteRepositoryCustom() {
		return new VoteRepositoryCustomImpl(jpaQueryFactory());
	}
}

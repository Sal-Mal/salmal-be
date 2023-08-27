package com.salmalteam.salmal.domain.vote;

import org.springframework.data.repository.Repository;

public interface VoteRepository extends Repository<Vote, Long>, VoteRepositoryCustom {
}

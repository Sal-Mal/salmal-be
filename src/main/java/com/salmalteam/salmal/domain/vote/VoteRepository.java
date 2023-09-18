package com.salmalteam.salmal.domain.vote;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface VoteRepository extends Repository<Vote, Long>, VoteRepositoryCustom {
    Vote save(Vote vote);
    Optional<Vote> findById(Long id);
}

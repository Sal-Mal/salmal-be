package com.salmalteam.salmal.domain.review;

import org.springframework.data.repository.Repository;

public interface ReviewRepository extends Repository<Review, Long>, ReviewRepositoryCustom {
}

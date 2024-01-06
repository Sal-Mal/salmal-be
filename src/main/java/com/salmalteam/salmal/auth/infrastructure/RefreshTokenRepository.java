package com.salmalteam.salmal.auth.infrastructure;

import com.salmalteam.salmal.auth.entity.RefreshToken;

import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}

package com.salmalteam.salmal.infra.auth;

import com.salmalteam.salmal.domain.auth.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}

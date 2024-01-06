package com.salmalteam.salmal.domain.auth.infrastructure;

import com.salmalteam.salmal.domain.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}

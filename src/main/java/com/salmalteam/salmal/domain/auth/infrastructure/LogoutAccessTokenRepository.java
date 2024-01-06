package com.salmalteam.salmal.domain.auth.infrastructure;

import com.salmalteam.salmal.domain.auth.entity.LogoutAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutAccessTokenRepository extends CrudRepository<LogoutAccessToken, String> {
}

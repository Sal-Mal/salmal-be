package com.salmalteam.salmal.infra.auth;

import com.salmalteam.salmal.domain.auth.LogoutAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutAccessTokenRepository extends CrudRepository<LogoutAccessToken, String> {
}

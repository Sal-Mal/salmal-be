package com.salmalteam.salmal.auth.infrastructure;

import com.salmalteam.salmal.auth.entity.LogoutAccessToken;

import org.springframework.data.repository.CrudRepository;

public interface LogoutAccessTokenRepository extends CrudRepository<LogoutAccessToken, String> {
}

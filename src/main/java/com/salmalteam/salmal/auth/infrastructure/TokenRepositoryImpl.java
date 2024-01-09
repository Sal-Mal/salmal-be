package com.salmalteam.salmal.auth.infrastructure;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.salmalteam.salmal.auth.entity.LogoutAccessToken;
import com.salmalteam.salmal.auth.entity.LogoutAccessTokenRepository;
import com.salmalteam.salmal.auth.entity.RefreshToken;
import com.salmalteam.salmal.auth.entity.RefreshTokenRepository;
import com.salmalteam.salmal.auth.entity.TokenRepository;

import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final RefreshTokenRepository refreshTokenRepository;
    private final LogoutAccessTokenRepository logoutAccessTokenRepository;

    @Override
    public void saveRefreshToken(final RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void saveLogoutAccessToken(final LogoutAccessToken logoutAccessToken) {
        logoutAccessTokenRepository.save(logoutAccessToken);
    }

    @Override
    public void deleteRefreshTokenById(final String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }

    @Override
    public boolean existsLogoutAccessTokenById(final String logoutAccessToken) {
        return logoutAccessTokenRepository.existsById(logoutAccessToken);
    }

    @Override
    public boolean existsRefreshTokenById(final String refreshToken) {
        return refreshTokenRepository.existsById(refreshToken);
    }
}

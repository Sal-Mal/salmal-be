package com.salmalteam.salmal.infra.auth;

import com.salmalteam.salmal.domain.auth.LogoutAccessToken;
import com.salmalteam.salmal.domain.auth.RefreshToken;
import com.salmalteam.salmal.domain.auth.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

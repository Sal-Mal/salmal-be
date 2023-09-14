package com.salmalteam.salmal.infra.auth;

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

    @Override
    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }
}

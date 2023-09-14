package com.salmalteam.salmal.infra.auth;

import com.salmalteam.salmal.application.auth.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider implements TokenProvider {

    private static final String TOKEN_TYPE = "Baerer";
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private final String ID = "id";
    private final long accessTokenExpiry;
    private final long refreshTokenExpiry;
    private final Key secretKey;

    public JwtProvider(@Value("${jwt.access-token-expiry}") final long accessTokenExpiry,
                       @Value("${jwt.refresh-token-expiry}") final long refreshTokenExpiry,
                       @Value("${jwt.secret-key}") final String secretKey) {
        this.accessTokenExpiry = accessTokenExpiry;
        this.refreshTokenExpiry = refreshTokenExpiry;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @Override
    public String createAccessToken(final Long id) {
        return createToken(id, accessTokenExpiry, ACCESS_TOKEN_SUBJECT);
    }

    @Override
    public String createRefreshToken(final Long id) {
        return createToken(id, refreshTokenExpiry, REFRESH_TOKEN_SUBJECT);
    }

    private String createToken(final Long id, long tokenExpiry,final String subject){
        final Date nowDate = new Date();
        final Date endDate = new Date(nowDate.getTime() + tokenExpiry);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(nowDate)
                .setExpiration(endDate)
                .claim(ID, id)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Long getTokenExpiry(final String token) {
        final Claims claims = getClaims(token);
        final Date expiry = claims.getExpiration();
        return expiry.getTime() - (new Date().getTime());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

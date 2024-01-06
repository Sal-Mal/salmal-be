package com.salmalteam.salmal.auth.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;
import java.util.concurrent.TimeUnit;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Token {

    @Id
    private String id;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiry;

    public Token(final String id, final Long expiry){
        this.id = id;
        this.expiry = expiry;
    }
}

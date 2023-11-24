package com.aroom.global.jwt.repository;

import com.aroom.global.jwt.model.JwtEntity;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public class JwtCacheRepository implements JwtRepository {
    private final Cache<String, String> tokenStorage;

    public JwtCacheRepository(@Value("${service.jwt.refresh-expiration}") long refreshTokenExpiration) {
        this.tokenStorage = CacheBuilder.newBuilder()
            .expireAfterWrite(refreshTokenExpiration, TimeUnit.MILLISECONDS)
            .build();
    }

    @Override
    public void save(JwtEntity entity) {
        tokenStorage.put(entity.key(), entity.value());
    }

    @Override
    public String getValueByKey(String key) {
        return tokenStorage.getIfPresent(key);
    }
}

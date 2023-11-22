package com.aroom.global.jwt.repository;

import com.aroom.global.jwt.model.JwtEntity;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

/**
 * 만약 해당 JwtRepository을 사용하고 싶다면 @Primary를 붙이세요
 * JwtCacheRepository의 @Primary는 제거해야 합니다.
 * @see com.aroom.global.jwt.repository.JwtCacheRepository
 */
@Repository
public class JwtRedisRepository implements JwtRepository {

    private final ValueOperations<String, String> opsForValue;

    public JwtRedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.opsForValue = redisTemplate.opsForValue();
    }

    @Override
    public void save(JwtEntity entity) {
        opsForValue.set(entity.key(), entity.value(), entity.expiration(), TimeUnit.MILLISECONDS);
    }

    @Override
    public String getValueByKey(String key) {
        return opsForValue.get(key);
    }
}

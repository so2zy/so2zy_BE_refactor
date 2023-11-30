package com.aroom.global.jwt.repository;

import com.aroom.global.jwt.model.JwtEntity;

public interface JwtRepository {

    void save(JwtEntity entity);

    String getValueByKey(String key);

}

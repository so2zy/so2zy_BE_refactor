package com.fastcampus.mini9.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(indexes = @Index(columnList = "tokenValue"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(unique = true)
    private String tokenValue;

    private String clientIp;

    private String userAgent;

    public RefreshToken(Long userId, String tokenValue, String clientIp, String userAgent) {
        this.userId = userId;
        this.tokenValue = tokenValue;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
    }

    public void update(Long userId, String tokenValue) {
        this.userId = userId;
        this.tokenValue = tokenValue;
    }
}

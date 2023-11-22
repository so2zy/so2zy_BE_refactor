package com.aroom.global.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.aroom.global.jwt.exception.BadTokenException;
import com.aroom.global.jwt.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    private final SecretKey secretKey;

    public JwtUtilsTest(@Value("${service.jwt.secret-key}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    @DisplayName("JWT Access Token 발급은")
    @Nested
    class Context_createAccessToken {

        @DisplayName("토큰 발급에 성공한다.")
        @Test
        void _willSuccess() {

            // given
            Date issueDate = new Date(System.currentTimeMillis());
            JwtPayload targetPayload = new JwtPayload("test@email.com", issueDate);

            // when
            String accessToken = jwtUtils.createToken(targetPayload, accessExpiration);

            // then
            assertThat(accessToken).isNotNull();

            Claims payload = Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(accessToken).getPayload();

            assertThat(payload.getIssuer()).isEqualTo(applicationName);
            assertThat(payload.getIssuedAt()).isEqualTo(roundOffMillis(issueDate));
            assertThat(payload.getExpiration())
                .isEqualTo(roundOffMillis(new Date(issueDate.getTime() + accessExpiration)));
        }

        @DisplayName("email 이 없다면 NPE가 발생한다.")
        @Test
        void ifEmailNull_NPE_willThrow() {

            // given
            Date issueDate = new Date(System.currentTimeMillis());
            JwtPayload targetPayload = new JwtPayload(null, issueDate);

            // when then
            assertThatThrownBy(() -> jwtUtils.createToken(targetPayload, accessExpiration))
                .isInstanceOf(NullPointerException.class);
        }

        @DisplayName("issuedAt 이 없다면 NPE가 발생한다.")
        @Test
        void ifIssueDateNull_NPE_willThrow() {

            // given
            JwtPayload targetPayload = new JwtPayload("test@email.com", null);

            // when then
            assertThatThrownBy(() -> jwtUtils.createToken(targetPayload, accessExpiration))
                .isInstanceOf(NullPointerException.class);
        }
    }

    @DisplayName("JWT 검증은")
    @Nested
    class Context_verifyToken {

        @DisplayName("만료 되지 않고, Secret Key가 일치하면 성공한다.")
        @Test
        void notExpired_equalsSecretKey_willSuccess() {

            // given
            Date issueDate = new Date(System.currentTimeMillis());
            JwtPayload targetPayload = new JwtPayload("test@email.com", issueDate);

            String accessToken = Jwts.builder()
                .claim("user-key", targetPayload.email())
                .issuer(applicationName)
                .issuedAt(issueDate)
                .expiration(new Date(issueDate.getTime() + accessExpiration))
                .signWith(secretKey, SIG.HS512)
                .compact();

            // when
            JwtPayload jwtPayload = jwtUtils.verifyToken(accessToken);

            // then
            assertThat(jwtPayload.email()).isEqualTo(targetPayload.email());
        }

        @DisplayName("SecretKey가 일치하지 않으면 BadTokenException가 발생한다.")
        @Test
        void notEqualsSecretKey_BadTokenException_willThrow() {

            // given
            Date issueDate = new Date(System.currentTimeMillis());
            JwtPayload targetPayload = new JwtPayload("test@email.com", issueDate);

            String accessToken = Jwts.builder()
                .claim("user-key", targetPayload.email())
                .issuer(applicationName)
                .issuedAt(issueDate)
                .expiration(new Date(issueDate.getTime() + accessExpiration))
                .signWith(SIG.HS256.key().build())
                .compact();

            // when
            assertThatThrownBy(() -> jwtUtils.verifyToken(accessToken))
                .isInstanceOf(BadTokenException.class);
        }

        @DisplayName("만료된 토큰이면 TokenExpiredException가 발생한다.")
        @Test
        void tokenExpired_TokenExpiredException_willThrow() {

            // given
            Date issueDate = new Date(System.currentTimeMillis());
            JwtPayload targetPayload = new JwtPayload("test@email.com", issueDate);

            String accessToken = Jwts.builder()
                .claim("user-key", targetPayload.email())
                .issuer(applicationName)
                .issuedAt(issueDate)
                .expiration(new Date(issueDate.getTime() - 10000))
                .signWith(secretKey, SIG.HS512)
                .compact();

            // when
            assertThatThrownBy(() -> jwtUtils.verifyToken(accessToken))
                .isInstanceOf(TokenExpiredException.class);
        }
    }

    // Jwts.issuedAt 의 경우 밀리초 자리수를 버리기 떄문에 해당 메서드를 사용해야 합니다.
    private Date roundOffMillis(Date date) {
        return new Date(date.getTime() / 1000 * 1000);
    }
}

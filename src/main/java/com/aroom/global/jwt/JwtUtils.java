package com.aroom.global.jwt;

import com.aroom.global.jwt.exception.BadTokenException;
import com.aroom.global.jwt.exception.TokenExpiredException;
import com.aroom.global.jwt.model.JwtPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Objects;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    private static final String USER_ID_KEY = "user-key";
    private static final String USER_NAME_KEY = "user-name";

    @Value("${spring.application.name}")
    private String issuer;

    private final SecretKey secretKey;

    public JwtUtils(@Value("${service.jwt.secret-key}") String rawSecretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(rawSecretKey));
    }

    public String createToken(JwtPayload jwtPayload, Date issuedAt, long expiration) {
        return Jwts.builder()
            // Gson 에서 Long 을 Double 로 Parse 하는 Issue가 있어서 String으로 Wrapping함.
            .claim(USER_ID_KEY, Objects.requireNonNull(jwtPayload.id().toString()))
            .claim(USER_NAME_KEY, Objects.requireNonNull(jwtPayload.name()))
            .issuer(issuer)
            .issuedAt(issuedAt)
            .expiration(new Date(issuedAt.getTime() + expiration))
            .signWith(secretKey, Jwts.SIG.HS512)
            .compact();
    }

    public JwtPayload verifyToken(String jwtToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(jwtToken);
            Claims payload = claimsJws.getPayload();
            return new JwtPayload(Long.parseLong(payload.get(USER_ID_KEY, String.class)), // Gson 에서 Long 을 Double 로 Parse 하는 Issue가 있어서 String으로 Wrapping함.8
                payload.get(USER_NAME_KEY, String.class));
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException(e.getMessage());
        } catch (JwtException e) {
            throw new BadTokenException(e.getMessage());
        }
    }
}

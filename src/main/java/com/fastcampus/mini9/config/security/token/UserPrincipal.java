package com.fastcampus.mini9.config.security.token;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.AuthenticatedPrincipal;

/**
 * AuthenticationPrincipalArgumentResolver가 바인딩한 결과로 뱉어주는 객체.
 */
@JsonIgnoreProperties({"name"})
public record UserPrincipal(Long id, String email) implements AuthenticatedPrincipal {

    public UserPrincipal(@JsonProperty("id") Long id, @JsonProperty("email") String email) {
        this.id = id;
        this.email = email;
    }

    @Override
    public String getName() {
        return this.email;
    }
}

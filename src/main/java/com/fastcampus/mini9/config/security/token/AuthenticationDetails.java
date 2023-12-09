package com.fastcampus.mini9.config.security.token;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class AuthenticationDetails extends WebAuthenticationDetails {

    private final String clientIp;
    private final String userAgent;

    public AuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.clientIp = extractClientIp(request);
        this.userAgent = request.getHeader("User-Agent");
    }

    private static String extractClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AuthenticationDetails that = (AuthenticationDetails) o;
        return Objects.equals(clientIp, that.clientIp) && Objects.equals(userAgent,
            that.userAgent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clientIp, userAgent);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(" [");
        sb.append("RemoteIpAddress=").append(this.getRemoteAddress()).append(", ");
        sb.append("SessionId=").append(this.getSessionId()).append(", ");
        sb.append("clientIp=").append(this.getClientIp()).append(", ");
        sb.append("userAgent=").append(this.getClientIp()).append("]");
        return sb.toString();
    }
}

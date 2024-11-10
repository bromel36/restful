package vn.hoidanit.jobhunter.util.constant;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstant {
    private static SecurityConstant instance;

    @Value("${hoidanit.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;

    @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    @Value("${hoidanit.jwt.base64-secret}")
    private String jwtKey;

    public SecurityConstant() {
    }

    @PostConstruct
    private void init() {
        instance = this;
    }

    public static long getAccessTokenExpiration() {
        return instance.accessTokenExpiration;
    }

    public static long getRefreshTokenExpiration() {
        return instance.refreshTokenExpiration;
    }

    public static String getJwtKey() {
        return instance.jwtKey;
    }
}

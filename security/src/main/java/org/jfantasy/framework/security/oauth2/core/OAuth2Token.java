package org.jfantasy.framework.security.oauth2.core;

import java.time.Instant;

public interface OAuth2Token {

    String getTokenValue();

    String getRefreshTokenValue();

    default Instant getIssuedAt() {
        return null;
    }

    default Instant getExpiresAt() {
        return null;
    }

}

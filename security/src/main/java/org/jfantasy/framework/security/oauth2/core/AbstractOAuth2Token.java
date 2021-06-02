package org.jfantasy.framework.security.oauth2.core;

import org.springframework.util.Assert;

import java.time.Instant;

public class AbstractOAuth2Token implements OAuth2Token {

    private final String tokenValue;
    private final Instant issuedAt;
    private Instant expiresAt;
    private String refreshTokenValue;

    protected AbstractOAuth2Token(String tokenValue) {
        this(tokenValue, null, null);
    }

    protected AbstractOAuth2Token(String tokenValue, Instant issuedAt, Instant expiresAt) {
        Assert.hasText(tokenValue, "tokenValue cannot be empty");
        if (issuedAt != null && expiresAt != null) {
            Assert.isTrue(expiresAt.isAfter(issuedAt), "expiresAt must be after issuedAt");
        }
        this.tokenValue = tokenValue;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    protected AbstractOAuth2Token(String tokenValue, String refreshTokenValue) {
        this(tokenValue, refreshTokenValue, null, null);
    }

    protected AbstractOAuth2Token(String tokenValue, String refreshTokenValue, Instant issuedAt, Instant expiresAt) {
        Assert.hasText(tokenValue, "tokenValue cannot be empty");
        if (issuedAt != null && expiresAt != null) {
            Assert.isTrue(expiresAt.isAfter(issuedAt), "expiresAt must be after issuedAt");
        }
        this.refreshTokenValue = refreshTokenValue;
        this.tokenValue = tokenValue;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    @Override
    public String getTokenValue() {
        return this.tokenValue;
    }


    @Override
    public Instant getIssuedAt() {
        return this.issuedAt;
    }

    @Override
    public Instant getExpiresAt() {
        return this.expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String getRefreshTokenValue() {
        return refreshTokenValue;
    }

    public void setRefreshTokenValue(String refreshTokenValue) {
        this.refreshTokenValue = refreshTokenValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        AbstractOAuth2Token other = (AbstractOAuth2Token) obj;
        if (!this.getTokenValue().equals(other.getTokenValue())) {
            return false;
        }
        if ((this.getIssuedAt() != null) ? !this.getIssuedAt().equals(other.getIssuedAt())
            : other.getIssuedAt() != null) {
            return false;
        }
        return (this.getExpiresAt() != null) ? this.getExpiresAt().equals(other.getExpiresAt())
            : other.getExpiresAt() == null;
    }

    @Override
    public int hashCode() {
        int result = this.getTokenValue().hashCode();
        result = 31 * result + ((this.getIssuedAt() != null) ? this.getIssuedAt().hashCode() : 0);
        result = 31 * result + ((this.getExpiresAt() != null) ? this.getExpiresAt().hashCode() : 0);
        return result;
    }
}

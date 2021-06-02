package org.jfantasy.framework.security.oauth2.core;

import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

public class OAuth2AccessToken extends AbstractOAuth2Token {

    private final TokenType tokenType;

    private final Set<String> scopes;

    public OAuth2AccessToken(TokenType tokenType, String tokenValue, Instant issuedAt, Instant expiresAt) {
        this(tokenType, tokenValue, issuedAt, expiresAt, Collections.emptySet());
    }

    public OAuth2AccessToken(TokenType tokenType, String tokenValue, Instant issuedAt, Instant expiresAt, Set<String> scopes) {
        super(tokenValue, issuedAt, expiresAt);
        Assert.notNull(tokenType, "tokenType cannot be null");
        this.tokenType = tokenType;
        this.scopes = Collections.unmodifiableSet((scopes != null) ? scopes : Collections.emptySet());
    }

    public OAuth2AccessToken(TokenType tokenType, String tokenValue, String refreshTokenValue, Instant issuedAt, Instant expiresAt, Set<String> scopes) {
        super(tokenValue, refreshTokenValue, issuedAt, expiresAt);
        Assert.notNull(tokenType, "tokenType cannot be null");
        this.tokenType = tokenType;
        this.scopes = Collections.unmodifiableSet((scopes != null) ? scopes : Collections.emptySet());
    }

    public TokenType getTokenType() {
        return this.tokenType;
    }

    public Set<String> getScopes() {
        return this.scopes;
    }

}

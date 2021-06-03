package org.jfantasy.framework.security.oauth2;

import lombok.SneakyThrows;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.oauth2.core.*;
import org.jfantasy.framework.security.oauth2.core.token.AuthorizationServerTokenServices;
import org.jfantasy.framework.security.oauth2.core.token.ConsumerTokenServices;
import org.jfantasy.framework.security.oauth2.core.token.ResourceServerTokenServices;
import org.jfantasy.framework.security.oauth2.jwt.JwtTokenService;
import org.jfantasy.framework.security.oauth2.jwt.JwtTokenServiceImpl;
import org.jfantasy.framework.security.oauth2.jwt.JwtUtils;
import org.jfantasy.framework.security.oauth2.server.authentication.BearerTokenAuthentication;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.util.DigestUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Token 服务
 *
 * @author limaofeng
 */
public class DefaultTokenServices implements AuthorizationServerTokenServices, ResourceServerTokenServices, ConsumerTokenServices {

    private TokenStore tokenStore;
    private ClientDetailsService clientDetailsService;
    private final JwtTokenService jwtTokenService = new JwtTokenServiceImpl();

    public DefaultTokenServices(TokenStore tokenStore, ClientDetailsService clientDetailsService) {
        this.tokenStore = tokenStore;
        this.clientDetailsService = clientDetailsService;
    }

    @SneakyThrows
    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) {
        LoginUser principal = (LoginUser) authentication.getPrincipal();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        ClientDetails clientDetails = this.clientDetailsService.loadClientByClientId(details.getClientId());

        int expires = clientDetails.getTokenExpires();
        String secret = clientDetails.getClientSecret();
        TokenType tokenType = details.getTokenType();

        boolean supportRefreshToken = false;
        Instant issuedAt = Instant.now();
        Instant expiresAt = null;

        if (tokenType == TokenType.PERSONAL) {
            expiresAt = details.getExpiresAt();
        } else if (tokenType == TokenType.TOKEN) {
            supportRefreshToken = true;
            expiresAt = Instant.now().plus(expires, ChronoUnit.MINUTES);
        } else if (tokenType == TokenType.SESSION) {
            expiresAt = Instant.now().plus(expires, ChronoUnit.MINUTES);
        }

        JwtTokenPayload payload = JwtTokenPayload.builder().uid(Long.valueOf(principal.getUid())).name(authentication.getName()).clientId(clientDetails.getClientId()).tokenType(tokenType).expiresAt(expiresAt).build();

        String tokenValue = generateTokenValue(payload, secret);

        OAuth2AccessToken accessToken = new OAuth2AccessToken(tokenType, tokenValue, issuedAt, expiresAt);

        if (supportRefreshToken) {
            String refreshTokenValue = generateRefreshTokenValue();
            OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(refreshTokenValue, issuedAt, expiresAt.plus(7, ChronoUnit.DAYS));
            tokenStore.storeRefreshToken(refreshToken, authentication);

            accessToken.setRefreshTokenValue(refreshTokenValue);
        }

        tokenStore.storeAccessToken(accessToken, authentication);

        return accessToken;
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        return null;
    }

    private void refreshAccessToken(OAuth2AccessToken accessToken, long expires) {
        accessToken.setExpiresAt(accessToken.getExpiresAt().plus(expires, ChronoUnit.MINUTES));
        this.tokenStore.storeAccessToken(accessToken, this.tokenStore.readAuthentication(accessToken.getTokenValue()));
    }

    @Override
    public boolean revokeToken(String tokenValue) {
        OAuth2AccessToken accessToken = this.readAccessToken(tokenValue);

        if (accessToken == null) {
            return false;
        }

        String refreshTokenValue = accessToken.getRefreshTokenValue();

        if (refreshTokenValue != null) {
            OAuth2RefreshToken refreshToken = this.tokenStore.readRefreshToken(refreshTokenValue);
            this.tokenStore.removeRefreshToken(refreshToken);
        }

        this.tokenStore.removeAccessToken(accessToken);
        return true;
    }

    @Override
    @SneakyThrows
    public BearerTokenAuthentication loadAuthentication(String accessToken) {
        OAuth2AccessToken token = this.readAccessToken(accessToken);
        return this.tokenStore.readAuthentication(token.getTokenValue());
    }

    @Override
    @SneakyThrows
    public OAuth2AccessToken readAccessToken(String accessToken) {
        // 解析内容
        JwtTokenPayload payload = JwtUtils.payload(accessToken);

        // 获取客户端配置
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(payload.getClientId());
        String secret = clientDetails.getClientSecret();
        int expires = clientDetails.getTokenExpires();

        // 验证 Token
        jwtTokenService.verifyToken(accessToken, DigestUtils.md5DigestAsHex(secret.getBytes()));

        // 获取令牌
        OAuth2AccessToken oAuth2AccessToken = this.tokenStore.readAccessToken(accessToken);

        if (oAuth2AccessToken == null) {
            throw new InvalidTokenException("无效的 Token");
        }

        // 如果续期方式为 Session 执行续期操作
        if (payload.getTokenType() == TokenType.SESSION) {
            this.refreshAccessToken(oAuth2AccessToken, expires);
        }

        return oAuth2AccessToken;
    }

    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public void setClientDetailsService(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    @SneakyThrows
    private String generateTokenValue(JwtTokenPayload payload, String secret) {
        String tokenValue;
        do {
            payload.setNonce(StringUtil.generateNonceString(32));
            tokenValue = jwtTokenService.generateToken(JSON.serialize(payload), DigestUtils.md5DigestAsHex(secret.getBytes()));
        } while (tokenStore.readAccessToken(tokenValue) != null);
        return tokenValue;
    }

    private String generateRefreshTokenValue() {
        String tokenValue;
        do {
            tokenValue = StringUtil.generateNonceString(32);
        } while (tokenStore.readRefreshToken(tokenValue) != null);
        return tokenValue;
    }
}

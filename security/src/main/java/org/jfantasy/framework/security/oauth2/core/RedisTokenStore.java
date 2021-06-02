package org.jfantasy.framework.security.oauth2.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.core.GrantedAuthority;
import org.jfantasy.framework.security.oauth2.server.BearerTokenAuthenticationToken;
import org.jfantasy.framework.security.oauth2.server.authentication.BearerTokenAuthentication;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.*;

/**
 * Redis 令牌存储器
 *
 * @author limaofeng
 */
@ConditionalOnClass(StringRedisTemplate.class)
@ConditionalOnMissingBean(TokenStore.class)
public class RedisTokenStore implements TokenStore, InitializingBean {

    private final String ASSESS_TOKEN_PREFIX = "assess_token:";
    private final String ASSESS_TOKEN_PRINCIPAL_PREFIX = "assess_token:principal:";
    private final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private final String REFRESH_TOKEN_PRINCIPAL_PREFIX = "refresh_token:principal:";
    private final String AUTHORIZATION_CODE_PREFIX = "authorization_code:";

    @Autowired
    private StringRedisTemplate redisTemplate;

    private ValueOperations valueOperations;
    private ListOperations listOperations;

    @Override
    public void afterPropertiesSet() throws Exception {
        valueOperations = redisTemplate.opsForValue();
        listOperations = redisTemplate.opsForList();
    }

    @Override
    public BearerTokenAuthentication readAuthentication(BearerTokenAuthenticationToken token) {
        return null;
    }

    @Override
    public BearerTokenAuthentication readAuthentication(String token) {
        String key = ASSESS_TOKEN_PREFIX + token;
        String data = redisTemplate.boundValueOps(key).get();
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        OAuth2AccessToken accessToken = buildOAuth2AccessToken(data);
        return buildBearerTokenAuthentication(data, accessToken);
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, Authentication authentication) {
        String data = buildStoreData(token, authentication);

        String key = ASSESS_TOKEN_PREFIX + token.getTokenValue();

        valueOperations.set(key, data);

        redisTemplate.expireAt(key, Date.from(token.getExpiresAt()));
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        String key = ASSESS_TOKEN_PREFIX + tokenValue;
        String data = redisTemplate.boundValueOps(key).get();
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        return buildOAuth2AccessToken(data);
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {

    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, Authentication authentication) {
//        String principal = principalToString(authentication);
//
//        String key = refreshToken.getTokenValue();
//
//        valueOperations.set(REFRESH_TOKEN_PREFIX + key, principal);
//
//        long expire = Duration.between(refreshToken.getExpiresAt(), refreshToken.getIssuedAt()).toMinutes();
//        redisTemplate.expire(REFRESH_TOKEN_PREFIX + key, expire, TimeUnit.MINUTES);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        String token = redisTemplate.boundValueOps(REFRESH_TOKEN_PREFIX + tokenValue).get();
        if (StringUtils.isEmpty(token)) {

        }
        return null;
    }

    @Override
    public BearerTokenAuthentication readAuthenticationForRefreshToken(OAuth2RefreshToken refreshToken) {
        String principal = redisTemplate.boundValueOps(REFRESH_TOKEN_PRINCIPAL_PREFIX + refreshToken.getTokenValue()).get();

        LoginUser user = JSON.deserialize(principal, LoginUser.class);

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, refreshToken.getTokenValue(), refreshToken.getIssuedAt(), refreshToken.getExpiresAt());
        return new BearerTokenAuthentication(user, accessToken, user.getAuthorities());
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        redisTemplate.delete(REFRESH_TOKEN_PRINCIPAL_PREFIX + token.getTokenValue());
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {

    }

    @Override
    public OAuth2AccessToken getAccessToken(BearerTokenAuthentication authentication) {
        return null;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        return null;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        return null;
    }

    private String buildStoreData(OAuth2AccessToken token, Authentication authentication) {
        Map<String, Object> data = new HashMap<>();
        data.put("access_token", token);
        data.put("principal", authentication.getPrincipal());
        data.put("authorities", ObjectUtil.defaultValue(authentication.getAuthorities(), () -> Collections.emptyList()));
        return JSON.serialize(data);
    }

    private OAuth2AccessToken buildOAuth2AccessToken(String data) {
        ObjectMapper mapper = JSON.getObjectMapper();
        ReadContext context = JsonPath.parse(data);
        String tokenValue = mapper.convertValue(context.read("$.access_token.tokenValue"), String.class);
        String refreshTokenValue = mapper.convertValue(context.read("$.access_token.refreshTokenValue"), String.class);
        Set<String> scopes = mapper.convertValue(context.read("$.access_token.scopes"), Set.class);
        Instant issuedAt = mapper.convertValue(context.read("$.access_token.issuedAt"), Instant.class);
        Instant expiresAt = mapper.convertValue(context.read("$.access_token.expiresAt"), Instant.class);

        return new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, tokenValue, refreshTokenValue, issuedAt, expiresAt, scopes);
    }

    private BearerTokenAuthentication buildBearerTokenAuthentication(String data, OAuth2AccessToken accessToken) {
        ObjectMapper mapper = JSON.getObjectMapper();
        ReadContext context = JsonPath.parse(data);

        LoginUser principal = mapper.convertValue(context.read("$.principal"), LoginUser.class);
        List<GrantedAuthority> authorities = mapper.convertValue(context.read("$.authorities"), new TypeReference<List<GrantedAuthority>>() {
        });

        return new BearerTokenAuthentication(principal, accessToken, authorities);
    }
}

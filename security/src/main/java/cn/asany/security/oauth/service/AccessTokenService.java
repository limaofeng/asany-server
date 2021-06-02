package cn.asany.security.oauth.service;

import cn.asany.security.core.bean.User;
import cn.asany.security.oauth.bean.AccessToken;
import cn.asany.security.oauth.bean.Application;
import cn.asany.security.oauth.dao.AccessTokenDao;
import org.jfantasy.framework.security.SecurityContextHolder;
import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.oauth2.DefaultTokenServices;
import org.jfantasy.framework.security.oauth2.JwtTokenPayload;
import org.jfantasy.framework.security.oauth2.core.*;
import org.jfantasy.framework.security.oauth2.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

/**
 * 访问令牌服务
 *
 * @author limaofeng
 */
@Service
public class AccessTokenService extends RedisTokenStore {

    private final static String RESOURCE_IDS = "URLRESOURCEIDS:";
    private final static String RESOURCE_PREFIX = "URLRESOURCEID:";
    private final static String ASSESS_TOKEN_PREFIX = "assess_token:";
    private final static String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private final static String AUTHORIZATION_CODE_PREFIX = "authorization_code:";
    private final static String ALL_PERMISSION = "ALL_PERMISSION:";

    @Autowired
    private AccessTokenDao accessTokenDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${spring.profiles.active}")
    private String env;
    @Autowired
    private DefaultTokenServices defaultTokenServices;

    @Override
    public void storeAccessToken(OAuth2AccessToken token, Authentication authentication) {
        Optional<AccessToken> optionalAccessToken = this.accessTokenDao.findById(token.getTokenValue());
        // 如果已经存在，更新最后使用时间及位置信息
        if (!optionalAccessToken.isPresent()) {
            JwtTokenPayload payload = JwtUtils.payload(token.getTokenValue());
            this.accessTokenDao.save(AccessToken.builder()
                .id(token.getTokenValue())
                .tokenType(token.getTokenType())
                .issuedAt(Date.from(token.getIssuedAt()))
                .expiresAt(Date.from(token.getExpiresAt()))
                .scopes(token.getScopes())
                .refreshToken(token.getRefreshTokenValue())
                .client(Application.builder().id(payload.getClientId()).build())
                .lastUsedTime(Date.from(Instant.now()))
                .user(User.builder().id(payload.getUid()).build())
                .build());
        } else {
            AccessToken accessToken = optionalAccessToken.get();
            accessToken.setExpiresAt(Date.from(token.getExpiresAt()));
            accessToken.setLastUsedTime(Date.from(Instant.now()));
        }

        super.storeAccessToken(token, authentication);
    }

    public AccessToken createPersonalAccessToken(String clientId, String name) {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = new OAuth2AuthenticationDetails();//TODO new OAuth2AuthenticationDetails(context.getRequest());
        oAuth2AuthenticationDetails.setClientId(clientId);
        oAuth2AuthenticationDetails.setTokenType(TokenType.SESSION);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(authentication, oAuth2AuthenticationDetails);
        OAuth2AccessToken accessToken = defaultTokenServices.createAccessToken(oAuth2Authentication);
        return this.accessTokenDao.getOne(accessToken.getTokenValue());
    }

//    @Transactional
//    public TokenResponse allocateToken(TokenRequest request) {
//        AccessToken accessToken = new AccessToken();
//        Optional<ApiKey> apiKeyOptional = appService.get(request.getApiKey());
//
//        if (!apiKeyOptional.isPresent()) {
//            throw new ValidationException("100501", " client_id 无效");
//        }

//        ApiKey apiKey = apiKeyOptional.get();

//        Long appId = apiKey.getApplication().getId();
//        accessToken.setApiKey(apiKey.getKey());
//        accessToken.setAppId("");
//        accessToken.setType(TokenType.BEARER);
//        accessToken.setGrantType(request.getGrantType());
//        accessToken.setRefreshToken(request.getRefreshToken());
//
//        if ("prod".equals(env)) {
//            accessToken.setExpires(7200);
//            accessToken.setReExpires(2592000);
//        } else {
//            accessToken.setExpires(120);
//            accessToken.setReExpires(1800);
//        }
//
//        ValueOperations valueOper = redisTemplate.opsForValue();
//        HashOperations hashOper = redisTemplate.opsForHash();
//        SetOperations setOper = redisTemplate.opsForSet();
//
//        LoginUser userDetails = new LoginUser();

    //new AuthUser(appId, apiKey.getApplication().getName(), apiKey.getKey(), apiKey.getDescription(), apiKey.getPlatform());

//        String token = UUID.randomUUID().toString() + "_" + request.getGrantType() + "_" + apiKey.getKey();
//
//        switch (request.getGrantType()) {
//            case AUTHORIZATION_CODE:
//                authorization_code(request.getCode(), accessToken);
//                break;
//            case CLIENT_CREDENTIALS:
//                client_credentials();
//                authService.retrieveUser(userDetails, apiKey);
//                break;
//            case PASSWORD:
//                password();
//                break;
//            case REFRESH_TOKEN:
//                AccessToken oldaccessToken = (AccessToken) hashOper.get(SecurityStorage.REFRESH_TOKEN_PREFIX + accessToken.getRefreshToken(), "token");
//                if (oldaccessToken == null) {
//                    throw new ValidationException(100503," refresh_token 无效");
//                }
//                if (apiKey.getKey().equals(oldaccessToken.getKey())) {
//                    throw new ValidationException(100504," apikey 与原值不匹配 ");
//                }
//                authService.retrieveUser(userDetails, (AuthUser) hashOper.get(SecurityStorage.REFRESH_TOKEN_PREFIX + accessToken.getRefreshToken(), "user"));
//                redisTemplate.delete(SecurityStorage.REFRESH_TOKEN_PREFIX + accessToken.getRefreshToken());
//                break;
//        }

//        accessToken.setKey(appId + "/" + DigestUtils.md5DigestAsHex(token.getBytes()));
//        accessToken.setRefreshToken(appId + "/" + DigestUtils.md5DigestAsHex((token + "RefreshToken").getBytes()));
//        accessToken.setTokenCreationTime(DateUtil.now());

//        String redisAccessTokenKey = ASSESS_TOKEN_PREFIX + accessToken.getKey();
//
//        //保存 AssessToken 与 UserDetails 到 redis
//        hashOper.put(redisAccessTokenKey, "token", accessToken);
//        hashOper.put(redisAccessTokenKey, "user", userDetails);
//        redisTemplate.expire(redisAccessTokenKey, accessToken.getExpires(), TimeUnit.SECONDS);
//
//        //保存 refreshToken 到 redis
//        String redisRefreshTokenKey = REFRESH_TOKEN_PREFIX + accessToken.getRefreshToken();
//        hashOper.put(redisRefreshTokenKey, "token", accessToken);
//        hashOper.put(redisRefreshTokenKey, "user", userDetails);
//        redisTemplate.expire(redisRefreshTokenKey, accessToken.getReExpires(), TimeUnit.SECONDS);

    //缓存 token 记录 避免重复生成 token 的问题
//        String key = apiKey.getKey() + userDetails.getScope() + userDetails.getId();
//        Set<String> tokens = setOper.members(key);
//        for (String _token : tokens) {
//            setOper.remove(key, _token);
//        }
//        tokens.add(key);
//        redisTemplate.delete(tokens);
//        setOper.add(key, redisAccessTokenKey, redisRefreshTokenKey);
//
//        return new TokenResponse(accessToken);
//    }

//
//    private void authorization_code(String code, AccessToken accessToken) {
//        ValueOperations valueOper = redisTemplate.opsForValue();
//        Object value = valueOper.get(AUTHORIZATION_CODE_PREFIX + code);
//        if (value == null) {
//            throw new ValidationException("100502", " authorization code 无效");
//        }
//        redisTemplate.delete(AUTHORIZATION_CODE_PREFIX + code);
//        if (value instanceof User) {
//            authService.retrieveUser(userDetails, (User) value);
//        } else if (value instanceof Member) {
//            authService.retrieveUser(userDetails, (Member) value);
//        }
//        token += ("_" + request.getCode());
//    }
//
//    public String implicit(String clientId, LoginUser loginUser) {
//        return null;
//    }
//
//    private void client_credentials(AuthCodeTokenRequest tokenRequest) {
//
//    }
//
//    private void password(AuthCodeTokenRequest tokenRequest) {
//
//    }
//
//    private void refresh_token() {
//    }


}

package cn.asany.security.oauth.service;

import cn.asany.security.oauth.bean.AccessToken;
import cn.asany.security.oauth.converter.AccessTokenConverter;
import cn.asany.security.oauth.dao.AccessTokenDao;
import cn.asany.security.oauth.vo.PersonalAccessToken;
import cn.asany.security.oauth.vo.SessionAccessToken;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.security.SecurityContextHolder;
import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.oauth2.DefaultTokenServices;
import org.jfantasy.framework.security.oauth2.JwtTokenPayload;
import org.jfantasy.framework.security.oauth2.core.OAuth2AccessToken;
import org.jfantasy.framework.security.oauth2.core.OAuth2Authentication;
import org.jfantasy.framework.security.oauth2.core.OAuth2AuthenticationDetails;
import org.jfantasy.framework.security.oauth2.core.TokenType;
import org.jfantasy.framework.security.oauth2.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 访问令牌服务
 *
 * @author limaofeng
 */
@Service
public class AccessTokenService {

    @Autowired
    private AccessTokenDao accessTokenDao;
    @Value("${spring.profiles.active}")
    private String env;
    @Autowired
    private DefaultTokenServices tokenServices;
    @Autowired
    private AccessTokenConverter accessTokenConverter;

    public PersonalAccessToken createPersonalAccessToken(String clientId, String name) {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = new OAuth2AuthenticationDetails();
        oAuth2AuthenticationDetails.setClientId(clientId);
        oAuth2AuthenticationDetails.setTokenType(TokenType.PERSONAL);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(authentication, oAuth2AuthenticationDetails);
        oAuth2Authentication.setName(name);
        OAuth2AccessToken accessToken = tokenServices.createAccessToken(oAuth2Authentication);
        return accessTokenConverter.toPersonalAccessToken(this.accessTokenDao.findOne(PropertyFilter.builder().equal("token", accessToken.getTokenValue()).build()).get());
    }

    public List<PersonalAccessToken> getPersonalAccessTokens(String clientId, Long uid) {
        PropertyFilterBuilder builder = PropertyFilter.builder()
            .equal("client.id", clientId)
            .equal("tokenType", TokenType.PERSONAL)
            .equal("user.id", uid);
        List<AccessToken> accessTokens = this.accessTokenDao.findAll(builder.build());
        return accessTokenConverter.toPersonalAccessTokens(accessTokens);
    }

    public List<SessionAccessToken> getSessions(String clientId, Long uid) {
        PropertyFilterBuilder builder = PropertyFilter.builder()
            .equal("client.id", clientId)
            .equal("tokenType", TokenType.SESSION)
            .equal("user.id", uid);
        List<AccessToken> accessTokens = this.accessTokenDao.findAll(builder.build());
        return accessTokenConverter.toSessions(accessTokens);
    }

    public boolean revokeToken(Long uid, String token) {
        JwtTokenPayload payload = JwtUtils.payload(token);
        OAuth2AccessToken accessToken = this.tokenServices.readAccessToken(token);
        if (!uid.equals(payload.getUid())) {
            return false;
        }
        return this.tokenServices.revokeToken(accessToken.getTokenValue());
    }

    public boolean revokeSession(Long uid, Long id) {
        PropertyFilterBuilder builder = PropertyFilter.builder()
            .equal("id", id)
            .equal("user.id", uid)
            .equal("tokenType", TokenType.SESSION);
        Optional<AccessToken> optionalAccessToken = this.accessTokenDao.findOne(builder.build());
        if (!optionalAccessToken.isPresent()) {
            return false;
        }
        return this.tokenServices.revokeToken(optionalAccessToken.get().getToken());
    }

    public boolean revokePersonalAccessToken(Long uid, Long id) {
        PropertyFilterBuilder builder = PropertyFilter.builder()
            .equal("id", id)
            .equal("user.id", uid)
            .equal("tokenType", TokenType.PERSONAL);
        Optional<AccessToken> optionalAccessToken = this.accessTokenDao.findOne(builder.build());
        if (!optionalAccessToken.isPresent()) {
            return false;
        }
        return this.tokenServices.revokeToken(optionalAccessToken.get().getToken());
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

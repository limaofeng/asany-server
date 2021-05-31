package cn.asany.security.oauth.service;

import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.security.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.concurrent.TimeUnit;

/**
 * 访问令牌服务
 *
 * @author limaofeng
 */
@Service
public class AccessTokenService {

    private final static String RESOURCE_IDS = "URLRESOURCEIDS:";
    private final static String RESOURCE_PREFIX = "URLRESOURCEID:";
    private final static String ASSESS_TOKEN_PREFIX = "assess_token:";
    private final static String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private final static String AUTHORIZATION_CODE_PREFIX = "authorization_code:";
    private final static String ALL_PERMISSION = "ALL_PERMISSION:";

    @Autowired
    private AppService appService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${spring.profiles.active}")
    private String env;

    @Transactional
    public TokenResponse allocateToken(TokenRequest request) {
        AccessToken accessToken = new AccessToken();
//        Optional<ApiKey> apiKeyOptional = appService.get(request.getApiKey());
//
//        if (!apiKeyOptional.isPresent()) {
//            throw new ValidationException("100501", " client_id 无效");
//        }

//        ApiKey apiKey = apiKeyOptional.get();

//        Long appId = apiKey.getApplication().getId();
//        accessToken.setApiKey(apiKey.getKey());
        accessToken.setAppId("");
        accessToken.setType(TokenType.BEARER);
        accessToken.setGrantType(request.getGrantType());
        accessToken.setRefreshToken(request.getRefreshToken());

        if ("prod".equals(env)) {
            accessToken.setExpires(7200);
            accessToken.setReExpires(2592000);
        } else {
            accessToken.setExpires(120);
            accessToken.setReExpires(1800);
        }

        ValueOperations valueOper = redisTemplate.opsForValue();
        HashOperations hashOper = redisTemplate.opsForHash();
        SetOperations setOper = redisTemplate.opsForSet();

        LoginUser userDetails = new LoginUser();

        //new AuthUser(appId, apiKey.getApplication().getName(), apiKey.getKey(), apiKey.getDescription(), apiKey.getPlatform());

//        String token = UUID.randomUUID().toString() + "_" + request.getGrantType() + "_" + apiKey.getKey();

        switch (request.getGrantType()) {
            case AUTHORIZATION_CODE:
                authorization_code(request.getCode(), accessToken);
                break;
            case CLIENT_CREDENTIALS:
//                client_credentials();
//                authService.retrieveUser(userDetails, apiKey);
                break;
            case PASSWORD:
//                password();
                break;
            case REFRESH_TOKEN:
//                AccessToken oldaccessToken = (AccessToken) hashOper.get(SecurityStorage.REFRESH_TOKEN_PREFIX + accessToken.getRefreshToken(), "token");
//                if (oldaccessToken == null) {
//                    throw new ValidationException(100503," refresh_token 无效");
//                }
//                if (apiKey.getKey().equals(oldaccessToken.getKey())) {
//                    throw new ValidationException(100504," apikey 与原值不匹配 ");
//                }
//                authService.retrieveUser(userDetails, (AuthUser) hashOper.get(SecurityStorage.REFRESH_TOKEN_PREFIX + accessToken.getRefreshToken(), "user"));
//                redisTemplate.delete(SecurityStorage.REFRESH_TOKEN_PREFIX + accessToken.getRefreshToken());
                break;
        }

//        accessToken.setKey(appId + "/" + DigestUtils.md5DigestAsHex(token.getBytes()));
//        accessToken.setRefreshToken(appId + "/" + DigestUtils.md5DigestAsHex((token + "RefreshToken").getBytes()));
//        accessToken.setTokenCreationTime(DateUtil.now());

        String redisAccessTokenKey = ASSESS_TOKEN_PREFIX + accessToken.getKey();

        //保存 AssessToken 与 UserDetails 到 redis
        hashOper.put(redisAccessTokenKey, "token", accessToken);
        hashOper.put(redisAccessTokenKey, "user", userDetails);
        redisTemplate.expire(redisAccessTokenKey, accessToken.getExpires(), TimeUnit.SECONDS);

        //保存 refreshToken 到 redis
        String redisRefreshTokenKey = REFRESH_TOKEN_PREFIX + accessToken.getRefreshToken();
        hashOper.put(redisRefreshTokenKey, "token", accessToken);
        hashOper.put(redisRefreshTokenKey, "user", userDetails);
        redisTemplate.expire(redisRefreshTokenKey, accessToken.getReExpires(), TimeUnit.SECONDS);

        //缓存 token 记录 避免重复生成 token 的问题
//        String key = apiKey.getKey() + userDetails.getScope() + userDetails.getId();
//        Set<String> tokens = setOper.members(key);
//        for (String _token : tokens) {
//            setOper.remove(key, _token);
//        }
//        tokens.add(key);
//        redisTemplate.delete(tokens);
//        setOper.add(key, redisAccessTokenKey, redisRefreshTokenKey);

        return new TokenResponse(accessToken);
    }


    private void authorization_code(String code, AccessToken accessToken) {
        ValueOperations valueOper = redisTemplate.opsForValue();
        Object value = valueOper.get(AUTHORIZATION_CODE_PREFIX + code);
        if (value == null) {
            throw new ValidationException("100502", " authorization code 无效");
        }
        redisTemplate.delete(AUTHORIZATION_CODE_PREFIX + code);
//        if (value instanceof User) {
//            authService.retrieveUser(userDetails, (User) value);
//        } else if (value instanceof Member) {
//            authService.retrieveUser(userDetails, (Member) value);
//        }
//        token += ("_" + request.getCode());
    }

    private void client_credentials(AuthCodeTokenRequest tokenRequest) {

    }

    private void password(AuthCodeTokenRequest tokenRequest) {

    }

    private void refresh_token() {
    }

}

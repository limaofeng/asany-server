package cn.asany.security.auth.authentication;

import org.jfantasy.framework.security.authentication.SimpleAuthenticationToken;

/**
 * 临时的身份验证令牌
 *
 * @author limaofeng
 */
public class TemporaryAuthenticationToken extends SimpleAuthenticationToken<String> {

    public TemporaryAuthenticationToken(String jobNumber) {
        super(jobNumber);
    }

}


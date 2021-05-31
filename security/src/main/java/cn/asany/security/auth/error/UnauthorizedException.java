package cn.asany.security.auth.error;

import org.jfantasy.framework.security.AuthenticationException;

/**
 * @author limaofeng
 */
public class UnauthorizedException extends AuthenticationException {

    public UnauthorizedException(String message) {
        super(message);
    }

}

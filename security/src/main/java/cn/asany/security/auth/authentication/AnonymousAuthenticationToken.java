package cn.asany.security.auth.authentication;

import org.jfantasy.framework.security.authentication.SimpleAuthenticationToken;

/**
 * 匿名凭证
 *
 * @author limaofeng
 */
public class AnonymousAuthenticationToken extends SimpleAuthenticationToken<String> {
    public AnonymousAuthenticationToken() {
        super("ANONYMOUS");
    }
}

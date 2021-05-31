package cn.asany.organization.utils;

import org.jfantasy.framework.security.DefaultSecurityContext;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SecurityContextHolder;

/**
 * @author xiongwei
 * @version 1.0
 * @date 2020/12/14 16:18
 */
public class LoginUtils {

    public static void clearPrincipal(){
        SecurityContextHolder.setContext(null);
    }

    public static void skipInterceptor(){
        SecurityContextHolder.setContext(new DefaultSecurityContext(LoginUser.builder().uid("99999").build()));
    }
}

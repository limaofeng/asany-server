package cn.asany.autoconfigure;

import org.jfantasy.framework.security.crypto.password.DESPasswordEncoder;
import org.jfantasy.framework.security.crypto.password.MD5PasswordEncoder;
import org.jfantasy.framework.security.crypto.password.PasswordEncoder;
import org.jfantasy.framework.security.crypto.password.PlaintextPasswordEncoder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author limaofeng
 */
@Configuration
@EntityScan({"cn.asany.security.*.bean"})
public class SecurityAutoConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder(Environment environment) {
        String encoder = environment.getProperty("PASSWORD_ENCODER", "plaintext");
        switch (encoder) {
            case "des":
                return new DESPasswordEncoder();
            case "plaintext":
                return new PlaintextPasswordEncoder();
            case "md5":
            default:
                return new MD5PasswordEncoder();
        }
    }

//    @Bean(name = "dingtalk.AuthenticationProvider")
//    public SimpleAuthenticationProvider dingtalkAuthenticationProvider(DingtalkUserDetailsService userDetailsService) {
//        SimpleAuthenticationProvider provider = new SimpleAuthenticationProvider(DingtalkAuthenticationToken.class);
//        provider.setUserDetailsService(userDetailsService);
//        return provider;
//    }

//    @Bean(name = "WeChat.AuthenticationProvider")
//    public SimpleAuthenticationProvider weChatAuthenticationProvider(WeChatUserDetailsService userDetailsService) {
//        SimpleAuthenticationProvider provider = new SimpleAuthenticationProvider(WeChatAuthenticationToken.class);
//        provider.setUserDetailsService(userDetailsService);
//        return provider;
//    }

//    @Bean(name = "anonymous.AuthenticationProvider")
//    public SimpleAuthenticationProvider anonymousAuthenticationProvider(AnonymousUserDetailsService userDetailsService) {
//        SimpleAuthenticationProvider provider = new SimpleAuthenticationProvider(AnonymousAuthenticationToken.class);
//        provider.setUserDetailsService(userDetailsService);
//        return provider;
//    }

//    @Bean(name = "temporary.AuthenticationProvider")
//    public SimpleAuthenticationProvider temporaryAuthenticationProvider(TemporaryUserDetailsService userDetailsService) {
//        SimpleAuthenticationProvider provider = new SimpleAuthenticationProvider(TemporaryAuthenticationToken.class);
//        provider.setUserDetailsService(userDetailsService);
//        return provider;
//    }

}

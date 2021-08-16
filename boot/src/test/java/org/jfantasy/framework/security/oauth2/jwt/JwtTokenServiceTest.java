package org.jfantasy.framework.security.oauth2.jwt;

import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

class JwtTokenServiceTest {

  @Test
  void generateToken() throws JOSEException {
    JwtTokenService tokenService = new JwtTokenServiceImpl();
    //        String secret = StringUtil.generateNonceString(24);
    String secret = DigestUtils.md5DigestAsHex("test".getBytes());
    String token = tokenService.generateToken("立马佛", secret);
    System.out.println("token:" + token);
  }

  @Test
  void verifyToken() {
    JwtTokenService tokenService = new JwtTokenServiceImpl();
  }
}

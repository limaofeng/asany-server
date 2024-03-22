package cn.asany.im.auth.service;

import cn.asany.im.TestApplication;
import cn.asany.im.auth.graphql.type.Platform;
import cn.asany.im.auth.service.vo.*;
import cn.asany.im.error.OpenIMServerAPIException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthServiceTest {

  @Autowired private AuthService authService;

  @BeforeEach
  void setUp() {}

  //  @Test
  //  void register() throws OpenIMServerAPIException {
  //    UserRegisterData data =
  //        authService.userRegister(
  //            UserRegisterRequestBody.builder()
  //                .userID("2")
  //                .secret("tuoyun")
  //                .platform(5)
  //                .phoneNumber("15921884771")
  //                .operationID(StringUtil.uuid())
  //                .build());
  //    log.debug(data.toString());
  //  }

  @Test
  void userToken() throws OpenIMServerAPIException {
    UserTokenData data =
        authService.userToken(
            UserTokenRequestBody.builder()
                .secret("tuoyun")
                .user("openIMAdmin")
                .platform(8)
                .build());
    log.debug(data.toString());
  }

  @Test
  void parseToken() throws OpenIMServerAPIException, InterruptedException {
    String _token = token("2");
    Thread.sleep(TimeUnit.SECONDS.toMillis(10));
    ParseTokenData data = authService.parseToken(_token, ParseTokenRequestBody.builder().build());
    log.debug(data.toString());
  }

  public String token(String userID) throws OpenIMServerAPIException {
    UserTokenData data =
        authService.userToken(
            UserTokenRequestBody.builder().secret("tuoyun").user(userID).platform(8).build());
    log.debug(data.toString());
    return data.getToken();
  }

  @Test
  void forceLogout() throws OpenIMServerAPIException {
    UserTokenData userToken =
        authService.userToken(
            UserTokenRequestBody.builder().secret("tuoyun").user("2").platform(5).build());
    log.debug(userToken.toString());

    this.authService.forceLogout(
        adminToken(), ForceLogoutRequestBody.builder().fromUserID("2").platform(5).build());

    ParseTokenData parseTokenData =
        this.authService.parseToken(userToken.getToken(), ParseTokenRequestBody.builder().build());
    log.debug(parseTokenData.toString());
  }

  public String adminToken() throws OpenIMServerAPIException {
    return token("openIMAdmin");
  }

  @Test
  void testToken() throws OpenIMServerAPIException {
    String _token = this.authService.token(Platform.IOS, "2");
    log.debug("token:" + _token);
    _token = this.authService.token(Platform.IOS, "2");
    log.debug("token:" + _token);
  }
}

package cn.asany.im.user.service;

import cn.asany.im.TestApplication;
import cn.asany.im.auth.service.AuthService;
import cn.asany.im.auth.service.vo.UserRegisterRequestBody;
import cn.asany.im.auth.service.vo.UserTokenData;
import cn.asany.im.auth.service.vo.UserTokenRequestBody;
import cn.asany.im.error.OpenIMServerAPIException;
import cn.asany.im.user.service.vo.*;
import cn.asany.security.core.domain.User;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
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
class UserServiceTest {

  @Autowired private AuthService authService;
  @Autowired private UserService userService;

  @Autowired private cn.asany.security.core.service.UserService userService1;

  public String adminToken() throws OpenIMServerAPIException {
    return token("openIMAdmin");
  }

  public String token(String userID) throws OpenIMServerAPIException {
    UserTokenData data =
        authService.userToken(
            UserTokenRequestBody.builder().secret("tuoyun").user(userID).platform(8).build());
    log.debug(data.toString());
    return data.getToken();
  }

  @Test
  void accountCheck() throws OpenIMServerAPIException {
    List<AccountCheckData> accountCheckDataList =
        this.userService.accountCheck(
            adminToken(), AccountCheckRequestBody.builder().checkUserID("15921884771").build());
    log.debug(accountCheckDataList.toString());
  }

  @Test
  void getAllUsersUid() throws OpenIMServerAPIException {
    List<String> usersUid =
        this.userService.getAllUsersUid(adminToken(), AllUsersUidRequestBody.builder().build());
    log.debug(usersUid.toString());
  }

  @Test
  void getSelfUserInfo() throws OpenIMServerAPIException {
    UserInfoData data =
        this.userService.getSelfUserInfo(
            token("3210490781"), GetSelfUserInfoRequestBody.builder().userID("3210490781").build());
    log.debug(data.toString());
  }

  @Test
  void getUsersInfo() throws OpenIMServerAPIException {
    List<GetUsersInfoData> data =
        this.userService.getUsersInfo(
            adminToken(),
            GetUsersInfoRequestBody.builder().userID("3210490781").userID("2").build());
    log.debug(data.toString());
  }

  @Test
  void getUsersOnlineStatus() throws OpenIMServerAPIException {
    token("2");
    token("3210490781");
    List<UsersOnlineStatusData> data =
        this.userService.getUsersOnlineStatus(
            adminToken(),
            GetUsersOnlineStatusRequestBody.builder().userID("3210490781").userID("2").build());
    log.debug(data.toString());
  }

  @Test
  void setGlobalMsgRecvOpt() throws OpenIMServerAPIException {
    this.userService.setGlobalMsgRecvOpt(
        token("2"), GlobalMsgRecvOptRequestBody.builder().globalRecvMsgOpt(0).build());
  }

  @Test
  void updateUserInfo() throws OpenIMServerAPIException {
    this.userService.updateUserInfo(
        adminToken(),
        UpdateUserInfoRequestBody.builder()
            .nickname("测试用户2")
            .phoneNumber("15921884771")
            .userID("2")
            .build());
  }

  @Test
  void syncToOpenIM() throws OpenIMServerAPIException {
    Optional<User> userOptional = userService1.findById(1L);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      UserRegisterRequestBody.UserRegisterRequestBodyBuilder builder =
          UserRegisterRequestBody.builder()
              .addUser(String.valueOf(user.getId()), user.getNickname(), user.getAvatar().getPath())
              .secret("tuoyun");

      userService.userRegister(builder.build());
    }
  }
}

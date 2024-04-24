package cn.asany.im.user.service;

import cn.asany.autoconfigure.properties.OpenIMProperties.AdminUser;
import cn.asany.im.auth.service.vo.UserRegisterData;
import cn.asany.im.auth.service.vo.UserRegisterRequestBody;
import cn.asany.im.auth.service.vo.UserRegisterResponseBody;
import cn.asany.im.error.OpenIMServerAPIException;
import cn.asany.im.error.ServerException;
import cn.asany.im.user.service.vo.*;
import cn.asany.im.utils.OpenIMUtils;
import java.util.Collections;
import java.util.List;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.jackson.JSON;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Service;

/**
 * 用户服务
 *
 * @author limaofeng
 */
@Slf4j
@Getter
@Service("im.UserService")
public class UserService {

  private final String url;
  private final String secret;
  private final AdminUser admin;

  public UserService(String url, String secret, AdminUser admin) {
    this.url = url;
    this.secret = secret;
    this.admin = admin;
  }

  /**
   * 用户注册
   *
   * @param request UserRegisterRequest
   * @return UserRegisterResponse
   */
  public UserRegisterData userRegister(UserRegisterRequestBody request)
      throws OpenIMServerAPIException {
    String url = this.url + "/user/user_register";
    try {
      if (StringUtil.isBlank(request.getSecret())) {
        request.setSecret(secret);
      }
      HttpResponse<UserRegisterResponseBody> response =
          Unirest.post(url)
              .header("operationID", createTraceId())
              .body(JSON.serialize(request))
              .asObject(UserRegisterResponseBody.class);
      return OpenIMUtils.getData(response.getBody());
    } catch (UnirestException e) {
      throw new ServerException(e.getMessage());
    }
  }

  /**
   * 用户注册
   *
   * @param token admin token
   * @param body UserRegisterRequest
   * @return UserRegisterResponse
   */
  public List<AccountCheckData> accountCheck(String token, AccountCheckRequestBody body)
      throws OpenIMServerAPIException {
    String url = this.url + "/user/account_check";
    try {
      HttpResponse<AccountCheckResponseBody> response =
          Unirest.post(url)
              .header("token", token)
              .body(JSON.serialize(body))
              .asObject(AccountCheckResponseBody.class);
      return OpenIMUtils.getData(response.getBody());
    } catch (UnirestException e) {
      throw new ServerException(e.getMessage());
    }
  }

  public List<String> getAllUsersUid(String token, AllUsersUidRequestBody body)
      throws OpenIMServerAPIException {
    String url = this.url + "/user/get_all_users_uid";
    try {
      HttpResponse<AllUsersUidResponseBody> response =
          Unirest.post(url)
              .header("token", token)
              .header("operationID", createTraceId())
              .body(JSON.serialize(body))
              .asObject(AllUsersUidResponseBody.class);
      return OpenIMUtils.getData(response.getBody());
    } catch (UnirestException e) {
      throw new ServerException(e.getMessage());
    }
  }

  public String createTraceId() {
    return StringUtil.uuid();
  }

  public UserInfoData getSelfUserInfo(String token, GetSelfUserInfoRequestBody body)
      throws OpenIMServerAPIException {
    String url = this.url + "/user/get_self_user_info";
    try {
      HttpResponse<GetSelfUserInfoResponseBody> response =
          Unirest.post(url)
              .header("token", token)
              .body(JSON.serialize(body))
              .asObject(GetSelfUserInfoResponseBody.class);
      return OpenIMUtils.getData(response.getBody());
    } catch (UnirestException e) {
      throw new ServerException(e.getMessage());
    }
  }

  public List<GetUsersInfoData> getUsersInfo(String token, GetUsersInfoRequestBody body)
      throws OpenIMServerAPIException {
    String url = this.url + "/user/get_users_info";
    try {
      HttpResponse<GetUsersInfoResponseBody> response =
          Unirest.post(url)
              .header("token", token)
              .body(JSON.serialize(body))
              .asObject(GetUsersInfoResponseBody.class);
      return OpenIMUtils.getData(response.getBody());
    } catch (UnirestException e) {
      throw new ServerException(e.getMessage());
    }
  }

  public List<UsersOnlineStatusData> getUsersOnlineStatus(
      String token, GetUsersOnlineStatusRequestBody body) throws OpenIMServerAPIException {
    String url = this.url + "/user/get_users_online_status";
    try {
      HttpResponse<GetUsersOnlineStatusResponseBody> response =
          Unirest.post(url)
              .header("token", token)
              .header("operationID", createTraceId())
              .body(JSON.serialize(body))
              .asObject(GetUsersOnlineStatusResponseBody.class);
      return ObjectUtil.defaultValue(
          OpenIMUtils.getData(response.getBody()), Collections.emptyList());
    } catch (UnirestException e) {
      throw new ServerException(e.getMessage());
    }
  }

  public void setGlobalMsgRecvOpt(String token, GlobalMsgRecvOptRequestBody body)
      throws OpenIMServerAPIException {
    String url = this.url + "/user/set_global_msg_recv_opt";
    try {
      HttpResponse<GlobalMsgRecvOptResponseBody> response =
          Unirest.post(url)
              .header("token", token)
              .body(JSON.serialize(body))
              .asObject(GlobalMsgRecvOptResponseBody.class);
      OpenIMUtils.getData(response.getBody());
    } catch (UnirestException e) {
      throw new ServerException(e.getMessage());
    }
  }

  public void updateUserInfo(String token, UpdateUserInfoRequestBody body)
      throws OpenIMServerAPIException {
    String url = this.url + "/user/update_user_info";
    try {
      HttpResponse<UpdateUserInfoResponseBody> response =
          Unirest.post(url)
              .header("token", token)
              .body(JSON.serialize(body))
              .asObject(UpdateUserInfoResponseBody.class);
      OpenIMUtils.getData(response.getBody());
    } catch (UnirestException e) {
      throw new ServerException(e.getMessage());
    }
  }
}

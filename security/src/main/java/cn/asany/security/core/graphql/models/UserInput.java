package cn.asany.security.core.graphql.models;

import cn.asany.security.core.graphql.inputs.GrantPermissionByUserInput;
import java.util.List;
import lombok.Data;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-06-17 15:36
 */
@Data
public class UserInput {

  //    public void setTel(String tel) {
  //        this.set("tel", tel);
  //    }

  /** 用户登录名称 */
  private String username;

  /** 密码 */
  private String password;

  /** 显示名称 */
  private String nickName;

  /** 角色 */
  private List<String> roles;
  /** 授予权限 */
  private List<GrantPermissionByUserInput> grants;

  /** 电话 */
  private String tel;

  /** 是否启用 */
  private Boolean enabled;

  /** 管理人员 */
  private Long employee;
}

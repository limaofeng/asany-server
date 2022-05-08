package cn.asany.security.core.graphql.input;

import cn.asany.security.core.bean.enums.Sex;
import cn.asany.storage.api.FileObject;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateInput {
  /** 姓名 */
  private String name;
  /** 头像 */
  private FileObject avatar;
  /** 电话 */
  private String phone;
  /** 邮箱 */
  private String email;
  /** 生日 */
  private Date birthday;
  /** 性别 */
  private Sex sex;
  /** 自我介绍 */
  private String bio;
  /** 公司 */
  private String company;
  /** 位置 */
  private String location;
}

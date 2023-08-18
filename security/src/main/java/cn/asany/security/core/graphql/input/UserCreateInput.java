package cn.asany.security.core.graphql.input;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户创建输入
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateInput implements Serializable {

  private String username;

  private String nickname;

  private String password;

  private List<String> tags;
}

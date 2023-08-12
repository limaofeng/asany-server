package cn.asany.security.core.graphql.input;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * 用户创建输入
 *
 * @author limaofeng
 */
@Data
@Builder
public class UserCreateInput {

  private String username;

  private String nickName;

  private List<String> tags;
}

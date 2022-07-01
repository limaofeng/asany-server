package cn.asany.nuwa.app.graphql.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 应用路由过滤器
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRouteFilter {
  private String space;
  private Boolean enabled;
}

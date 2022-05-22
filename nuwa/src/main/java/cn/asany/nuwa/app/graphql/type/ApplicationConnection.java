package cn.asany.nuwa.app.graphql.type;

import cn.asany.nuwa.app.bean.Application;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * 应用分页对象
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ApplicationConnection
    extends BaseConnection<ApplicationConnection.ApplicationEdge, Application> {

  private List<ApplicationEdge> edges;

  @Data
  public static class ApplicationEdge implements Edge<Application> {
    private String cursor;
    private Application node;
  }
}

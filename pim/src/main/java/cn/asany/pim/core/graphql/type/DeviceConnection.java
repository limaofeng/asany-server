package cn.asany.pim.core.graphql.type;

import cn.asany.pim.core.domain.Device;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * 角色查询接口
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceConnection extends BaseConnection<DeviceConnection.DeviceEdge, Device> {

  private List<DeviceEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DeviceEdge implements Edge<Device> {
    private String cursor;
    private Device node;
  }
}

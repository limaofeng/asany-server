package cn.asany.storage.data.graphql.type;

import cn.asany.storage.api.FileObject;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.graphql.Edge;
import net.asany.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = false)
public class FileObjectConnection
    extends BaseConnection<FileObjectConnection.FileObjectEdge, FileObject> {
  private List<FileObjectEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class FileObjectEdge implements Edge<FileObject> {
    private String cursor;
    private FileObject node;
  }
}

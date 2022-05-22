package cn.asany.storage.data.graphql.type;

import cn.asany.storage.api.FileObject;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

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

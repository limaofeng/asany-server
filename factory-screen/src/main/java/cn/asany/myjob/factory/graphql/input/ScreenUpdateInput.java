package cn.asany.myjob.factory.graphql.input;

import cn.asany.storage.api.FileObject;
import java.util.List;
import lombok.Data;

@Data
public class ScreenUpdateInput {
  private String description;

  private String status;

  private FileObject statusImage;

  private FileObject info;

  private List<FileObject> operators;

  private List<FileObject> documents;
}

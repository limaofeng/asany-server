package cn.asany.cms.learn.graphql.inputs;

import cn.asany.cms.learn.domain.enums.LessonType;
import cn.asany.storage.api.FileObject;
import java.util.List;
import lombok.Data;

@Data
public class LessonInput {
  private Long course;
  private LessonType lessonType;
  private String title;
  private String content;
  private List<Long> channels;
  private List<FileObject> attachments;
  private String organization;
}

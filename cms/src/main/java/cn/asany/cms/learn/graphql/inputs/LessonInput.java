package cn.asany.cms.learn.graphql.inputs;

import cn.asany.cms.article.bean.enums.ArticleCategory;
import cn.asany.cms.learn.bean.enums.LessonType;
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
  private ArticleCategory category;
  private List<FileObject> attachments;
  private String organization;
}

package cn.asany.cms.learn.graphql.inputs;

import cn.asany.storage.api.FileObject;
import java.util.List;

import lombok.Data;

@Data
public class CourseInput {

  private String name;
  private String type;
  private String introduction;
  private float duration;
  private List<String> learnerScope;
  private FileObject cover;
  private Long publishUser;
  private String notificationType;
  private String controlType;
  private Boolean top;
}

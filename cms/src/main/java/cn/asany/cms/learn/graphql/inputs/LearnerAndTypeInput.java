package cn.asany.cms.learn.graphql.inputs;

import cn.asany.cms.learn.domain.enums.LearnerType;
import lombok.Data;

/** @Date 2019/11/28 9:57 @Description TODO */
@Data
public class LearnerAndTypeInput {
  private Long learner;
  private LearnerType learnerType;
}

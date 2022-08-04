package cn.asany.cms.learn.graphql.inputs;

import cn.asany.cms.learn.domain.enums.LearnerType;
import lombok.Data;

/** @date 2022/7/28 9:12 9:12 TODO */
@Data
public class LearnerAndTypeInput {
  private Long learner;
  private LearnerType learnerType;
}

package cn.asany.cms.learn.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearnerTrans {

  private Long courseId;

  private String employeeId;
}

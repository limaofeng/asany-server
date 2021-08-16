package cn.asany.security.core.graphql.types;

import java.util.Date;
import lombok.Data;

@Data
public class PositionDTO {
  private Long id;

  private Date createTime;

  private String creator;

  private String modifier;

  private Date modifyTime;

  private String description;

  private String name;

  private Long departmentId;

  private Long jobId;

  private String organizationId;
}

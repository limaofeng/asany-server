package cn.asany.organization.core.service.dto;

import java.util.List;
import lombok.Data;

/**
 * 员工状态
 *
 * @author limaofeng
 */
@Data
public class NuwaEmployeeIdentity {
  private String dimension;
  private String status;
  private String department;
  private String position;
  private List<NuwaEmployeePosition> positions;
}

package cn.asany.organization.core.service.dto;

import java.util.List;
import lombok.Data;

/**
 * 组织导入对象
 *
 * @author limaofeng
 */
@Data
public class NuwaOrganization {
  private String code;
  private String name;
  private List<NuwaOrgDimension> dimensions;
  private List<NuwaEmployee> employees;
}

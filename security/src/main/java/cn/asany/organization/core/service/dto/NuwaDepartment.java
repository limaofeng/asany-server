package cn.asany.organization.core.service.dto;

import java.util.List;
import lombok.Data;

/**
 * 部门导入对象
 *
 * @author limaofeng
 */
@Data
public class NuwaDepartment {
  private String code;
  private String name;
  private String type;
  private List<String> positions;
  private List<NuwaDepartment> children;
}

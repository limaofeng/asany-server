package cn.asany.organization.core.service.dto;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 员工对象
 *
 * @author limaofeng
 */
@Data
public class NuwaEmployee {
  private String jobNumber;
  private String name;
  private Date birthday;
  private String sex;
  private List<String> phones;
  private List<String> emails;
  private List<NuwaEmployeeIdentity> identities;
}

package cn.asany.security.core.service.dto;

import cn.asany.security.core.domain.PermissionPolicy;
import java.util.List;
import lombok.Data;

@Data
public class ImportPermissionPolicies {
  private List<PermissionPolicy> policies;
}

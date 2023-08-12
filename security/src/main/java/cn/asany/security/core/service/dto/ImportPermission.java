package cn.asany.security.core.service.dto;

import cn.asany.security.core.domain.PermissionStatement;
import java.util.List;
import lombok.Data;

@Data
public class ImportPermission {
  private String id;
  private String name;
  private String description;
  private List<PermissionStatement> permissionStatements;
}

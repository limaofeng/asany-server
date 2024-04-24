package cn.asany.security.core.service;

import cn.asany.security.core.dao.PermissionPolicyDao;
import cn.asany.security.core.dao.PermissionStatementDao;
import cn.asany.security.core.domain.PermissionPolicy;
import cn.asany.security.core.domain.PermissionStatement;
import cn.asany.security.core.domain.enums.PermissionPolicyType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 权限策略服务
 *
 * @author limaofeng
 */
@Service
public class PermissionPolicyService {

  private final PermissionPolicyDao permissionPolicyDao;
  private final PermissionStatementDao permissionStatementDao;

  //  private final PermissionService permissionService;
  //  private final GrantPermissionService grantPermissionService;

  public PermissionPolicyService(
      PermissionPolicyDao permissionPolicyDao, PermissionStatementDao permissionStatementDao) {
    this.permissionPolicyDao = permissionPolicyDao;
    this.permissionStatementDao = permissionStatementDao;
    //    this.grantPermissionService = grantPermissionService;
  }

  public PermissionPolicy loadPolicy(String name) {
    return permissionPolicyDao
        .findOne(PropertyFilter.newFilter().equal("name", name))
        .orElseThrow(() -> new NotFoundException("权限策略不存在：" + name));
  }

  public Page<PermissionPolicy> findPage(Pageable pageable, PropertyFilter filter) {
    return this.permissionPolicyDao.findPage(pageable, filter);
  }

  @Transactional(rollbackFor = Exception.class)
  public void importPermissionPolicies(List<PermissionPolicy> policies) {
    List<PermissionStatement> unusedStatements = new ArrayList<>();
    this.permissionPolicyDao.saveAllInBatch(
        policies.stream()
            .map(
                item ->
                    permissionPolicyDao
                        .findOne(
                            PropertyFilter.newFilter()
                                .equal("name", item.getName())
                                .equal("type", PermissionPolicyType.SYSTEM))
                        .map(
                            policy -> {
                              unusedStatements.addAll(policy.getStatements());
                              policy.setStatements(
                                  item.getStatements().stream()
                                      .peek(stm -> stm.setPolicy(policy))
                                      .collect(Collectors.toList()));
                              return policy;
                            })
                        .orElseGet(
                            () -> {
                              item.setType(PermissionPolicyType.SYSTEM);
                              item.getStatements().forEach(stm -> stm.setPolicy(item));
                              return item;
                            }))
            .collect(Collectors.toList()));
    this.permissionStatementDao.deleteAllInBatch(unusedStatements);
  }
}

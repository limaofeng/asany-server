package cn.asany.security.core.service;

import cn.asany.security.core.dao.GroupDao;
import cn.asany.security.core.domain.Group;
import cn.asany.security.core.domain.GroupPrimaryKey;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 用户组服务
 *
 * @author limaofeng
 */
@Service
public class GroupService {

  private final GroupDao groupDao;

  public GroupService(GroupDao groupDao) {
    this.groupDao = groupDao;
  }

  public Page<Group> findPage(Pageable pageable, PropertyFilter filter) {
    return this.groupDao.findPage(pageable, filter);
  }

  public Optional<Group> findById(GroupPrimaryKey id) {
    return this.groupDao.findById(id);
  }

  public Group update(Long id, Group group) {
    return null;
  }

  public void deleteUserGroup(Long id) {}

  public Group save(Group group) {
    return null;
  }

  public List<Long> addMembersToGroup(Long userGroupId, Long... members) {
    return null;
  }
}

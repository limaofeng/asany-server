package cn.asany.security.core.service;

import cn.asany.security.core.dao.GroupDao;
import cn.asany.security.core.dao.GroupMemberDao;
import cn.asany.security.core.domain.Group;
import cn.asany.security.core.domain.enums.GranteeType;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.spring.SpringBeanUtils;
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
  private final GroupMemberDao groupMemberDao;

  public GroupService(GroupDao groupDao, GroupMemberDao groupMemberDao) {
    this.groupDao = groupDao;
    this.groupMemberDao = groupMemberDao;
  }

  public Page<Group> findPage(Pageable pageable, PropertyFilter filter) {
    return this.groupDao.findPage(pageable, filter);
  }

  public Optional<Group> findById(Long id) {
    return this.groupDao.findById(id);
  }

  public Group update(Long id, Group group) {
    return null;
  }

  public void deleteGroup(Long id) {}

  public Group save(Group group) {
    return null;
  }

  public List<Long> addMembersToGroup(Long groupId, Long... members) {
    return null;
  }

  public List<Group> findAllByUser(Long user) {
    Set<Long> groupIds =
        this.groupMemberDao
            .findAll(
                PropertyFilter.newFilter()
                    .equal("type", GranteeType.USER)
                    .equal("value", String.valueOf(user)))
            .stream()
            .map(item -> item.getGroup().getId())
            .collect(Collectors.toSet());
    GroupService self = SpringBeanUtils.getBean(GroupService.class);
    return groupIds.stream()
        .map(self::findById)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }
}

package cn.asany.security.core.service;

import cn.asany.security.core.bean.GrantPermission;
import cn.asany.security.core.bean.Permission;
import cn.asany.security.core.bean.PermissionType;
import cn.asany.security.core.bean.enums.ResourceType;
import cn.asany.security.core.dao.GrantPermissionDao;
import cn.asany.security.core.dao.PermissionDao;
import cn.asany.security.core.dao.PermissionTypeDao;
import cn.asany.security.core.exception.ValidDataException;
import cn.asany.security.core.util.GrantPermissionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author limaofeng
 */
@Service
@Transactional
public class PermissionService {

    private final PermissionDao permissionDao;

    @Autowired
    private PermissionTypeDao permissionTypeDao;

    @Autowired
    private GrantPermissionDao grantPermissionDao;

    @Autowired
    public PermissionService(PermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    public Pager<Permission> findPager(Pager<Permission> pager, List<PropertyFilter> filters) {
        return this.permissionDao.findPager(pager, filters);
    }

    public Permission save(Permission permission) {
        if (permissionDao.existsById(permission.getId())) {
            throw new ValidDataException(ValidDataException.PERMISSION_EXISTS, permission.getId());
        }
        return this.permissionDao.save(permission);
    }

    public Permission update(String id, Boolean merge, Permission permission) {
        if (!permissionDao.existsById(id)) {
            throw new ValidDataException(ValidDataException.PERMISSION_NOTEXISTS, id);
        }
        permission.setId(id);
        return permissionDao.save(permission);
    }

    public Permission get(String id) {
        Optional<Permission> optional = this.permissionDao.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public void delete(String... ids) {
        for (String id : ids) {
            if (!permissionDao.existsById(id)) {
                throw new ValidDataException(ValidDataException.PERMISSION_NOTEXISTS, id);
            }
            grantPermissionDao.deleteGrantPermissionByPermissionId(id);
            grantPermissionDao.flush();
            permissionDao.deleteById(id);
        }
    }

    private List<Permission> loadPermissionByUrl() {
        return this.permissionDao.findAll((root, query, builder) -> builder.and(builder.equal(root.get("resource.type"), ResourceType.url)));
    }

    public List<Permission> findByRoleId(String roleId) {
        return this.permissionDao.findAll((root, query, builder) -> builder.equal(root.get("roles.id"), roleId));
    }

    public List<Permission> find(Example example) {
        return this.permissionDao.findAll(example);
    }

    public Pager<PermissionType> findTypePager(Pager<PermissionType> pager, List<PropertyFilter> filters) {
        return this.permissionTypeDao.findPager(pager, filters);
    }

    public PermissionType savePermissionType(PermissionType permissionType) {
        if (permissionTypeDao.existsById(permissionType.getId())) {
            throw new ValidDataException(ValidDataException.PERMITYPE_EXISTS, permissionType.getId());
        }
        return permissionTypeDao.save(permissionType);
    }

    public PermissionType updatePermissionType(String id, Boolean merge, PermissionType permissionType) {
        if (!permissionTypeDao.existsById(id)) {
            throw new ValidDataException(ValidDataException.PERMITYPE_NOTEXISTS, id);
        }
        permissionType.setId(id);
        return permissionTypeDao.save(permissionType);
    }

    public void deletePermissionType(String id) {
        if (!permissionTypeDao.existsById(id)) {
            throw new ValidDataException(ValidDataException.PERMITYPE_NOTEXISTS, id);
        }
        PermissionType permissionType = permissionTypeDao.getOne(id);
        if (CollectionUtils.isEmpty(permissionType.getPermissions()) || permissionType.getPermissions().size() == 0) {
            permissionTypeDao.delete(permissionType);
        } else {
            throw new ValidDataException(ValidDataException.PERMITYPE_HAS_PERMISSIONS, id);
        }
    }

    public List<Permission> permission(String resourceType, String resourceId ){
        return GrantPermissionUtils.getPermissions(resourceType, resourceId);
    }
}

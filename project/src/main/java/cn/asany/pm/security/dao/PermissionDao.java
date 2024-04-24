package cn.asany.pm.security.dao;

/**
 * @author limaofeng@msn.com @ClassName: PermissionDao @Description: 权限列表的Dao(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
import cn.asany.pm.security.bean.Permission;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository("issuePermissionsDao")
public interface PermissionDao extends AnyJpaRepository<Permission, Long> {}

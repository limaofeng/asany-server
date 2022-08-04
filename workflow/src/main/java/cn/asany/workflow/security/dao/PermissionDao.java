package cn.asany.workflow.security.dao;

/**
 * @author limaofeng@msn.com @ClassName: PermissionDao @Description: 权限列表的Dao(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
import cn.asany.pm.security.bean.Permission;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("issuePermissionsDao")
public interface PermissionDao extends JpaRepository<Permission, Long> {}

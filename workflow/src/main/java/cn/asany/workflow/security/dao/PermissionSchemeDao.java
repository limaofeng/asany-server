package cn.asany.workflow.security.dao;

/**
 * @author penghanying @ClassName: PermissionSchemeDao @Description: 权限方案的Dao(这里用一句话描述这个类的作用)
 * @date 2019/5/31
 */
import net.whir.hos.issue.security.bean.PermissionScheme;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("issuePermissionSchemeDao")
public interface PermissionSchemeDao extends JpaRepository<PermissionScheme, Long> {}

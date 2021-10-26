package cn.asany.workflow.security.dao;

/**
 * @author penghanying @ClassName: GrantPermissionDao @Description: (这里用一句话描述这个类的作用)
 * @date 2019/5/31
 */
import net.whir.hos.issue.security.bean.GrantPermission;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("issueGrantPermissionDao")
public interface GrantPermissionDao extends JpaRepository<GrantPermission, Long> {}

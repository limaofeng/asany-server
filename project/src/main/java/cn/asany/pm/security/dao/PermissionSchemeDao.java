package cn.asany.pm.security.dao;

/**
 * @author limaofeng@msn.com @ClassName: PermissionSchemeDao @Description: 权限方案的Dao(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
import cn.asany.pm.security.bean.PermissionScheme;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository("issuePermissionSchemeDao")
public interface PermissionSchemeDao extends AnyJpaRepository<PermissionScheme, Long> {}

package cn.asany.workflow.screen.dao;

/**
 * @author limaofeng@msn.com @ClassName: FieldToScreenDao @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
import cn.asany.pm.screen.bean.FieldToScreen;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldToScreenDao extends AnyJpaRepository<FieldToScreen, Long> {}

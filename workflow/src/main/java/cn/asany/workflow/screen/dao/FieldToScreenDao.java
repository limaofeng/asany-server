package cn.asany.workflow.screen.dao;

/**
 * @author penghanying @ClassName: FieldToScreenDao @Description: (这里用一句话描述这个类的作用)
 * @date 2019/5/23
 */
import net.whir.hos.issue.screen.bean.FieldToScreen;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldToScreenDao extends JpaRepository<FieldToScreen, Long> {}

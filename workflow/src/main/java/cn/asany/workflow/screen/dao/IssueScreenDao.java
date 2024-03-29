package cn.asany.workflow.screen.dao;

/**
 * @author limaofeng@msn.com @ClassName: IssueScreenDao @Description: 页面的dao(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
import cn.asany.pm.screen.bean.IssueScreen;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueScreenDao extends JpaRepository<IssueScreen, Long> {}

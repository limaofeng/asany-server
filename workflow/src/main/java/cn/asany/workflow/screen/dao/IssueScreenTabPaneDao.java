package cn.asany.workflow.screen.dao;

/**
 * @author limaofeng@msn.com @ClassName: IssueScreenTabPaneDao @Description:
 *     页面中tabPane的dao(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
import cn.asany.pm.screen.bean.IssueScreenTabPane;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueScreenTabPaneDao extends JpaRepository<IssueScreenTabPane, Long> {}

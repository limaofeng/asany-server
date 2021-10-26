package cn.asany.workflow.field.dao;

/**
 * @author penghanying @ClassName: IssueFieldDao @Description: (这里用一句话描述这个类的作用)
 * @date 2019/5/23
 */
import net.whir.hos.issue.field.bean.IssueField;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueFieldDao extends JpaRepository<IssueField, Long> {}

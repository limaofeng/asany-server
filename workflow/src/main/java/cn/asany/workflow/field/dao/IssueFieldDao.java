package cn.asany.workflow.field.dao;

/**
 * @author limaofeng@msn.com @ClassName: IssueFieldDao @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
import cn.asany.pm.field.bean.IssueField;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueFieldDao extends AnyJpaRepository<IssueField, Long> {}

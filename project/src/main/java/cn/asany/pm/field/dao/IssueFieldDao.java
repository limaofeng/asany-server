package cn.asany.pm.field.dao;

/**
 * @author limaofeng@msn.com @ClassName: IssueFieldDao @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
import cn.asany.pm.field.bean.Field;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueFieldDao extends JpaRepository<Field, Long> {}
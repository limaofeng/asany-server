package cn.asany.workflow.field.dao;

import cn.asany.pm.field.bean.IssueFieldConfiguration;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface IssueFieldConfigurationDao extends AnyJpaRepository<IssueFieldConfiguration, Long> {}

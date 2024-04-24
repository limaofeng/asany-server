package cn.asany.pm.project.dao;

import cn.asany.pm.project.domain.Project;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 添加项目文件
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-05-24 09:45
 */
@Repository
public interface ProjectDao extends AnyJpaRepository<Project, Long> {}

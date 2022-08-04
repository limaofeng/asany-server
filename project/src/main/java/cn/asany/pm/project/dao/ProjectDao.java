package cn.asany.pm.project.dao;

import cn.asany.pm.project.domain.Project;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 添加项目文件
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-05-24 09:45
 */
@Repository
public interface ProjectDao extends JpaRepository<Project, Long> {}

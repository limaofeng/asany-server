package cn.asany.pm.project.service;

import cn.asany.pm.project.dao.ProjectDao;
import cn.asany.pm.project.domain.Project;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工单
 *
 * @author limaofeng
 * @date 2019-05-24 09:45
 */
@Service
public class ProjectService {

  private final ProjectDao projectDao;

  public ProjectService(ProjectDao projectDao) {
    this.projectDao = projectDao;
  }

  @Transactional(readOnly = true)
  public Optional<Project> get(Long id) {
    return this.projectDao.findById(id);
  }

  public List<Project> findAll() {
    return this.projectDao.findAll();
  }

  @Transactional
  public Project save(Project project) {
    return this.projectDao.save(project);
  }

  public Project findById(Long id) {
    return this.projectDao.getReferenceById(id);
  }

  @Transactional(readOnly = true)
  public Page<Project> findPage(Pageable pageable, PropertyFilter filter) {
    return this.projectDao.findPage(pageable, filter);
  }
}

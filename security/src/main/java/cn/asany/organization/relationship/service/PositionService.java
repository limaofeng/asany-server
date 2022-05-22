package cn.asany.organization.relationship.service;

import cn.asany.organization.core.bean.Department;
import cn.asany.organization.core.bean.Job;
import cn.asany.organization.core.dao.DepartmentDao;
import cn.asany.organization.core.dao.JobDao;
import cn.asany.organization.relationship.bean.Position;
import cn.asany.organization.relationship.dao.PositionDao;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-05-10 12:01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PositionService {
  @Autowired private PositionDao positionDao;
  @Autowired private JobDao jobDao;
  @Autowired private DepartmentDao departmentDao;

  public Position save(Long departmentId, Long jobId) {
    Optional<Position> optional =
        this.positionDao.findOne(
            Example.of(
                Position.builder()
                    .job(Job.builder().id(jobId).build())
                    .department(Department.builder().id(departmentId).build())
                    .build()));

    Position.PositionBuilder builder = Position.builder();

    Department department = departmentDao.getOne(departmentId);
    Job job = jobDao.getOne(jobId);

    builder
        .department(department)
        .job(job)
        .description(job.getDescription())
        .name(job.getName())
        .organization(department.getOrganization())
        .build();
    if (optional.isPresent()) {
      builder.id(optional.get().getId());
      return this.positionDao.update(builder.build(), true);
    } else {
      return this.positionDao.save(builder.build());
    }
  }

  public Position findByDepAndJob(Long departmentId, Long jobId) {
    Optional<Position> optional =
        this.positionDao.findOne(
            Example.of(
                Position.builder()
                    .job(Job.builder().id(jobId).build())
                    .department(Department.builder().id(departmentId).build())
                    .build()));
    return optional.orElse(null);
  }

  public Position get(Long id) {
    Optional<Position> optional =
        this.positionDao.findOne(Example.of(Position.builder().id(id).build()));
    return optional.orElse(null);
  }

  public boolean delete(Long id) {
    this.positionDao.deleteById(id);
    return true;
  }

  public List<Position> findAll(List<PropertyFilter> builder) {
    return this.positionDao.findAll(builder, Sort.by("createdAt").descending());
  }
}

package cn.asany.organization.core.service;

import cn.asany.organization.core.bean.Job;
import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.core.dao.JobDao;
import cn.asany.organization.relationship.bean.Position;
import cn.asany.organization.relationship.dao.EmployeePositionDao;
import cn.asany.organization.relationship.dao.PositionDao;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 职务
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-05-10 12:01
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class JobService {
  @Autowired private JobDao jobDao;
  @Autowired private PositionDao positionDao;
  @Autowired private EmployeePositionDao employeePositionDao;

  public List<Job> findAll(Long org, Sort orderBy) {
    if (orderBy.isUnsorted()) {
      orderBy = Sort.by("createdAt").descending();
    }
    return this.jobDao.findAll(
        Example.of(Job.builder().organization(Organization.builder().id(org).build()).build()),
        orderBy);
  }

  public Job save(Job job) {
    Optional<Job> old =
        job.getId() != null
            ? this.jobDao.findById(job.getId())
            : this.jobDao.findOne(
                Example.of(
                    Job.builder().organization(job.getOrganization()).name(job.getName()).build()));
    if (old.isPresent()) {
      job.setId(old.get().getId());
      return this.jobDao.update(job, true);
    } else {
      return this.jobDao.save(job);
    }
  }

  public boolean delete(Long id) {
    List<Position> positions = positionDao.findByJob(Job.builder().id(id).build());
    positions.forEach(
        position -> {
          //            this.employeePositionDao.removeEmployeePositionByPosition(position);
        });
    this.positionDao.deleteByJob(id);
    this.jobDao.deleteById(id);
    return true;
  }

  public Job get(Long id) {
    Optional<Job> byId = jobDao.findById(id);
    return byId.orElse(null);
  }
}

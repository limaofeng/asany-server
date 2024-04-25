/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.organization.relationship.service;

import cn.asany.organization.core.dao.DepartmentDao;
import cn.asany.organization.core.dao.JobDao;
import cn.asany.organization.core.domain.Department;
import cn.asany.organization.core.domain.Job;
import cn.asany.organization.relationship.dao.PositionDao;
import cn.asany.organization.relationship.domain.Position;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
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

  public List<Position> findAll(PropertyFilter filter) {
    return this.positionDao.findAll(filter, Sort.by("createdAt").descending());
  }
}

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
package cn.asany.organization.core.service;

import cn.asany.organization.core.dao.OrganizationMemberDao;
import cn.asany.organization.core.domain.OrganizationMember;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationMemberService {

  private final OrganizationMemberDao organizationMemberDao;

  public OrganizationMemberService(OrganizationMemberDao organizationMemberDao) {
    this.organizationMemberDao = organizationMemberDao;
  }

  @Transactional(readOnly = true)
  public Optional<OrganizationMember> findOneByUserAndOrganization(Long user, Long organization) {
    return this.organizationMemberDao.findOne(
        PropertyFilter.newFilter().equal("user.id", user).equal("organization.id", organization));
  }
}

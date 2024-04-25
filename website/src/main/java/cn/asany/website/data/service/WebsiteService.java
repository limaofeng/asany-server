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
package cn.asany.website.data.service;

import cn.asany.website.data.dao.WebsiteDao;
import cn.asany.website.data.domain.Website;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class WebsiteService {

  private final WebsiteDao websiteDao;

  public WebsiteService(WebsiteDao websiteDao) {
    this.websiteDao = websiteDao;
  }

  public List<Website> websites(PropertyFilter filter, Sort sort) {
    return this.websiteDao.findAll(filter, sort);
  }

  public Website save(Website website) {
    if (website.getChannel() == null) {
      // 自动创建栏目
      System.out.println(" Website getChannel ");
    }
    return this.websiteDao.save(website);
  }

  public Website get(Long id) {
    return this.websiteDao.getReferenceById(id);
  }
}

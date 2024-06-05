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
package cn.asany.pim.core.service;

import cn.asany.pim.core.dao.WarrantyCardDao;
import cn.asany.pim.core.domain.WarrantyCard;
import org.springframework.stereotype.Service;

@Service
public class WarrantyCardService {

  private final WarrantyCardDao warrantyCardDao;

  public WarrantyCardService(WarrantyCardDao warrantyCardDao) {
    this.warrantyCardDao = warrantyCardDao;
  }

  public void save(WarrantyCard warrantyCard) {
    this.warrantyCardDao.save(warrantyCard);
  }

  public void update(WarrantyCard warrantyCard) {
    this.warrantyCardDao.update(warrantyCard);
  }
}

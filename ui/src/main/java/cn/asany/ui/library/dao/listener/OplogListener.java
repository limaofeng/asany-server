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
package cn.asany.ui.library.dao.listener;

import cn.asany.ui.library.domain.enums.Operation;
import cn.asany.ui.library.service.OplogService;
import jakarta.persistence.*;
import net.asany.jfantasy.framework.spring.SpringBeanUtils;

public class OplogListener {

  private OplogService _oplogService;

  private OplogService getOplogService() {
    if (this._oplogService == null) {
      this._oplogService = SpringBeanUtils.getBeanByType(OplogService.class);
    }
    return this._oplogService;
  }

  @PostPersist
  public void postPersist(Object source) {
    OplogService oplogService = getOplogService();
    oplogService.log(Operation.INSERT, source);
  }

  @PostUpdate
  public void postUpdate(Object source) {
    OplogService oplogService = getOplogService();
    oplogService.log(Operation.UPDATE, source);
  }

  @PostRemove
  public void postRemove(Object source) {
    OplogService oplogService = getOplogService();
    oplogService.log(Operation.DELETE, source);
  }
}

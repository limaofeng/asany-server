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
package cn.asany.crm.support.graphql.input;

import cn.asany.base.common.graphql.input.ContactInformationInput;
import cn.asany.crm.support.domain.enums.TicketPriority;
import cn.asany.storage.api.FileObject;
import java.util.List;
import lombok.Data;

@Data
public class TicketCreateInput {
  /** 工单描述 */
  private String description;

  /** 附件 */
  private List<FileObject> attachments;

  /** 工单类型 */
  private Long type;

  /** 工单优先级 */
  private TicketPriority priority;

  /** 客户ID */
  private Long customerId;

  /** 门店ID */
  private Long storeId;

  /** 联系方式 */
  private ContactInformationInput contactInfo;

  /** 工单目标 */
  private TicketTargetInput target;
}

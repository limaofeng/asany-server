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
package cn.asany.pim.core.graphql.input;

import cn.asany.base.common.graphql.input.AddressInput;
import cn.asany.base.common.graphql.input.ContactInformationInput;
import java.util.Date;
import lombok.Data;

@Data
public class DeviceCreateInput {
  private String no;

  /** 设备序列号 */
  private String sn;

  /** 设备名称 */
  private String name;

  /** 设备二维码 */
  private String qrcode;

  /** 设备型号 */
  private Long productId;

  /** 购买日期 */
  private Date purchaseDate;

  /** 设备保修政策 */
  private Long warrantyPolicyId;

  /** 设备保修开始日期 */
  private Date warrantyStartDate;

  /** 设备保修结束日期 */
  private Date warrantyEndDate;

  /** 设备位置 */
  private AddressInput address;

  /** 设备联系信息 */
  private ContactInformationInput contactInfo;

  /** 设备所有者 */
  private DeviceOwnerInput owner;
}

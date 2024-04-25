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
package cn.asany.drive.domain;

import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CLOUD_DRIVE_QUOTA")
public class CloudDriveQuota extends BaseBusEntity {
  @Id
  @Column(name = "DRIVE_ID", nullable = false, updatable = false, precision = 22)
  @GenericGenerator(
      name = "CloudDriveQuotaPkGenerator",
      strategy = "foreign",
      parameters = {@Parameter(name = "property", value = "cloudDrive")})
  @GeneratedValue(generator = "CloudDriveQuotaPkGenerator")
  private Long id;

  /** 文件数 */
  @Column(name = "`COUNT`")
  private Integer count;

  /** 已使用 */
  @Column(name = "`USAGE`")
  private Long usage;

  /** 总大小 */
  @Column(name = "SIZE")
  private Long size;

  /** 云盘 */
  @OneToOne(fetch = FetchType.LAZY, targetEntity = CloudDrive.class, mappedBy = "quota")
  @ToString.Exclude
  private CloudDrive cloudDrive;
}

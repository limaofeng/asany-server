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
package cn.asany.cardhop.contacts.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 自定义字段
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2022-3-15 上午11:18:06
 */
public class ContactAttribute extends BaseBusEntity {

  private static final long serialVersionUID = 3862476075020180077L;

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /**
   * 所有者类型<br>
   * 1.全部<br>
   * 2.个人<br>
   * 3.部门<br>
   * 4.角色<br>
   * 5.联系薄<br>
   */
  private String ownerType;

  /** 所有者 */
  private String owner;

  /**
   * 字段应用位置<br>
   * 1.联系人<br>
   * 2.地址<br>
   * 3.邮箱<br>
   * 4.电话<br>
   */
  private String position;

  /** 字段名称 */
  private String name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getOwnerType() {
    return ownerType;
  }

  public void setOwnerType(String ownerType) {
    this.ownerType = ownerType;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }
}

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
package cn.asany.crm.support.domain.enums;

import lombok.Getter;

@Getter
public enum TicketPriority {
  LOW("低"),
  NORMAL("普通"),
  HIGH("高"),
  URGENT("紧急");

  private final String name;

  TicketPriority(String name) {
    this.name = name;
  }
}

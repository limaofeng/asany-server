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
package cn.asany.ui.library.domain;

import cn.asany.ui.library.OplogDataCollector;
import cn.asany.ui.library.dao.listener.OplogListener;
import cn.asany.ui.library.domain.enums.LibraryType;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@NamedEntityGraph(
    name = "Graph.Library.FetchIcon",
    attributeNodes = {
      @NamedAttributeNode(value = "items", subgraph = "SubGraph.LibraryItem.FetchIconResource")
    },
    subgraphs = {
      @NamedSubgraph(
          name = "SubGraph.LibraryItem.FetchIconResource",
          attributeNodes = {
            @NamedAttributeNode(value = "tags"),
            @NamedAttributeNode(value = "icon")
          }),
    })
@NamedEntityGraph(
    name = "Graph.Library.FetchComponent",
    attributeNodes = {
      @NamedAttributeNode(value = "items", subgraph = "SubGraph.LibraryItem.FetchComponentResource")
    },
    subgraphs = {
      @NamedSubgraph(
          name = "SubGraph.LibraryItem.FetchComponentResource",
          attributeNodes = {
            @NamedAttributeNode(value = "tags"),
            @NamedAttributeNode(value = "component")
          }),
    })
@Entity
@Table(
    name = "UI_LIBRARY",
    uniqueConstraints =
        @UniqueConstraint(
            columnNames = {"TYPE", "NAME"},
            name = "UK_LIBRARY_NAME"))
@EntityListeners(value = {OplogListener.class})
public class Library extends BaseBusEntity implements OplogDataCollector {
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private LibraryType type;

  /** 名称 */
  @Column(name = "NAME", length = 60)
  private String name;

  /** 描述 */
  @Column(name = "DESCRIPTION")
  private String description;

  /** 库内资源 */
  @OneToMany(
      mappedBy = "library",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
      fetch = FetchType.LAZY)
  private Set<LibraryItem> items;

  /** 所有者 */
  //  @Any(
  //      metaColumn =
  //          @Column(name = "OWNERSHIP_TYPE", length = 10, insertable = false, updatable = false),
  //      fetch = FetchType.LAZY)
  //  @AnyMetaDef(
  //      idType = "long",
  //      metaType = "string",
  //      metaValues = {
  //        @MetaValue(targetEntity = User.class, value = User.OWNERSHIP_KEY),
  //        @MetaValue(targetEntity = Organization.class, value = Organization.OWNERSHIP_KEY)
  //      })
  //  @JoinColumn(name = "OWNERSHIP_ID", insertable = false, updatable = false)
  //  private Ownership ownership;

  @Override
  @Transient
  public String getEntityName() {
    if (this.type == null) {
      return null;
    }
    return this.type.getName();
  }
}

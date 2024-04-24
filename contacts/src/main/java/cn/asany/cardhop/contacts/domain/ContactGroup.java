package cn.asany.cardhop.contacts.domain;

import cn.asany.organization.core.domain.databind.DepartmentDeserializer;
import cn.asany.organization.core.domain.databind.DepartmentSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 联系人群组
 *
 * @author 李茂峰
 * @since 2022-3-15 上午11:29:20
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "ContactGroup")
@Table(name = "CARDHOP_CONTACT_GROUP")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "book", "contacts"})
public class ContactGroup extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 20)
  private String name;

  /** 命名空间 */
  @Column(name = "NAMESPACE", nullable = false, length = 200)
  private String namespace;

  /** 路径 */
  @Column(name = "PATH", length = 50)
  private String path;

  /** 排序字段 */
  @Column(name = "SORT")
  private Integer index;

  /** 分组人数统计 */
  @Column(name = "COUNT")
  private Long count;

  /** 层级 */
  @Column(name = "LEVEL")
  private Integer level;

  /** 描述信息 */
  @Column(name = "DESCRIPTION", length = 150)
  private String description;

  /** 上级分组 */
  @JsonSerialize(using = DepartmentSerializer.class)
  @JsonDeserialize(using = DepartmentDeserializer.class)
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  @JoinColumn(name = "PID", foreignKey = @ForeignKey(name = "FK_CONTACTS_GROUP_PID"))
  private ContactGroup parent;

  /** 下属分组 */
  @JsonInclude(content = JsonInclude.Include.NON_NULL)
  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @OrderBy("sort ASC")
  private List<ContactGroup> children;

  /** 所属通讯录 */
  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {jakarta.persistence.CascadeType.REFRESH})
  @JoinColumn(name = "BOOK_ID")
  private ContactBook book;

  @ManyToMany(targetEntity = Contact.class, fetch = FetchType.LAZY)
  @JoinTable(
      name = "CARDHOP_CONTACT_GROUP_CONTACT",
      joinColumns = @JoinColumn(name = "GROUP_ID"),
      inverseJoinColumns = @JoinColumn(name = "CONTACT_ID"))
  private List<Contact> contacts;
}

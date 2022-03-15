package cn.asany.cardhop.contacts.bean;

import cn.asany.base.common.Ownership;
import cn.asany.cardhop.contacts.bean.enums.ContactBookType;
import cn.asany.organization.core.bean.Organization;
import cn.asany.security.core.bean.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.MetaValue;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 通信录(Address Book)
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2022-3-15 上午11:53:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "ContactBook")
@Table(name = "CARDHOP_CONTACT_BOOK")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class ContactBook extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Column(name = "NAME", length = 20)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private ContactBookType type;

  /** 所有者 */
  @Any(
      metaColumn = @Column(name = "OWNER_TYPE", length = 20, insertable = false, updatable = false),
      fetch = FetchType.LAZY)
  @AnyMetaDef(
      idType = "long",
      metaType = "string",
      metaValues = {
        @MetaValue(targetEntity = User.class, value = User.OWNERSHIP_KEY),
        @MetaValue(targetEntity = Organization.class, value = Organization.OWNERSHIP_KEY)
      })
  @JoinColumn(name = "OWNER", insertable = false, updatable = false)
  private Ownership owner;
  /** 所有联系人 */
  @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
  @OrderBy("createdAt DESC")
  private List<Contact> contacts;
  /** 联系人分组列表 */
  @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
  @OrderBy("createdAt ASC")
  private List<ContactGroup> groups;
}

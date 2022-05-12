package cn.asany.cardhop.contacts.bean;

import cn.asany.security.core.bean.enums.Sex;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 联系人表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2022-3-15 上午11:11:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CARDHOP_CONTACT")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "book", "groupIds"})
public class Contact extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 所属通讯录 */
  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {javax.persistence.CascadeType.REFRESH})
  @JoinColumn(name = "BOOK_ID", foreignKey = @ForeignKey(name = "FK_CARDHOP_CONTACT_BOOK"))
  private ContactBook book;
  /** 联系人照片 */
  @Convert(converter = FileObjectConverter.class)
  @Column(name = "AVATAR", length = 500)
  private FileObject avatar;
  /** 姓名 */
  @Column(name = "NAME", length = 20)
  private String name;
  /** 性别 */
  @Enumerated(EnumType.STRING)
  @Column(name = "SEX", length = 20)
  private Sex sex;
  /** 公司 */
  @Column(name = "COMPANY", length = 200)
  private String company;
  /** 部门 */
  @Column(name = "DEPARTMENT", length = 200)
  private String department;
  /** 职位 */
  @Column(name = "TITLE", length = 50)
  private String title;
  /** 工号 */
  @Column(name = "JOB_NUMBER", length = 200)
  private String jobNumber;
  /** 电话列表 */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "contact", cascade = CascadeType.REMOVE)
  private List<ContactPhoneNumber> phones;
  /** 邮箱列表 */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "contact", cascade = CascadeType.REMOVE)
  private List<ContactEmail> emails;
  /** 地址列表 */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "contact", cascade = CascadeType.REMOVE)
  private List<ContactAddress> addresses;
  /** 网址 */
  @Column(name = "WEBSITE", length = 50)
  private String website;
  /** 备注 */
  @Column(name = "DESCRIPTION", length = 2000)
  private String description;
  /**
   * 所属分组<br>
   * 多个分组以;分割
   */
  @ManyToMany(targetEntity = ContactGroup.class, fetch = FetchType.LAZY)
  @JoinTable(
      name = "CARDHOP_CONTACT_GROUP_CONTACT",
      joinColumns = @JoinColumn(name = "CONTACT_ID"),
      inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
  private List<ContactGroup> groups;
}

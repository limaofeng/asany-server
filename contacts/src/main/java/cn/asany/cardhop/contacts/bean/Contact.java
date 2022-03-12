package cn.asany.cardhop.contacts.bean;

import cn.asany.security.core.bean.enums.Sex;
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
 * @since 2013-3-15 上午11:11:59
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
  @JoinColumn(name = "BOOK_ID")
  private ContactBook book;
  /** 联系人照片 */
  @Column(name = "AVATAR", length = 200)
  private String avatar;
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
  /** 移动电话 */
  @Column(name = "MOBILE", length = 20)
  private String mobile;
  /** E-mail */
  @Column(name = "EMAIL", length = 50)
  private String email;
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
      name = "CONTACTS_GROUP_LINKMAN",
      joinColumns = @JoinColumn(name = "LINKMAN_ID"),
      inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
  private List<ContactGroup> groups;

  @Transient private String groupNames;
}

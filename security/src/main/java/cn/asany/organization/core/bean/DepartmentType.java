package cn.asany.organization.core.bean;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * 部门类型
 *
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019/9/2 1:57 下午
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_DEPARTMENT_TYPE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DepartmentType implements Serializable {
  @Id
  @Column(name = "ID", precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 组织 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference
  @JoinColumn(
      name = "ORGANIZATION_ID",
      foreignKey = @ForeignKey(name = "FK_ORG_DEPARTMENT_TYPE_OID"))
  private Organization organization;
  /** 编码 */
  @Column(name = "CODE", length = 20)
  private String code;

  /** 是否支持多部门 */
  @Column(name = "MULTI_SECTORAL")
  private Boolean multiSectoral;

  /** 最大兼岗人数 */
  @Column(name = "AND_POST")
  private Long andPost;

  public Boolean getMultiSectoral() {
    return multiSectoral;
  }
}

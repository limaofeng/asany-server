package cn.asany.shanhai.core.domain;

import cn.asany.base.usertype.FileUserType;
import cn.asany.storage.api.FileObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.Type;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

/**
 * 模型
 *
 * @author limaofeng
 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "SH_MODULE")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Module extends BaseBusEntity {
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 编码 */
  @Column(name = "CODE", length = 50, unique = true)
  private String code;

  /** 名称 */
  @Column(name = "NAME", length = 100)
  private String name;

  /** 模块图片 */
  @Type(FileUserType.class)
  @Column(name = "PICTURE", length = 500)
  private FileObject picture;

  /** 描述 */
  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  /** 模块 */
  @OneToMany(mappedBy = "module", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  @ToString.Exclude
  private Set<Model> models;
}

package cn.asany.shanhai.core.domain;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Set;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

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
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 编码 */
  @Column(name = "CODE", length = 50, unique = true)
  private String code;
  /** 名称 */
  @Column(name = "NAME", length = 100)
  private String name;
  /** 模块图片 */
  @Convert(converter = FileObjectConverter.class)
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

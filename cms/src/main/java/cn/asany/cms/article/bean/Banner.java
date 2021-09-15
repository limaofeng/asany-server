package cn.asany.cms.article.bean;

import cn.asany.cms.article.bean.enums.MediaType;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.lucene.annotations.Indexed;

/**
 * 横幅
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Indexed
@Entity
@Table(name = "CMS_BANNER")
public class Banner extends BaseBusEntity {
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 标题 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 标题 */
  @Column(name = "TITLE", length = 200)
  private String title;
  /** 描述 */
  @Column(name = "description", length = 500)
  private String description;
  /** 媒介 */
  @Column(name = "MEDIA", length = 20)
  @Enumerated(EnumType.STRING)
  private MediaType mediaType;
  /** 媒介 */
  @Column(name = "MEDIA", length = 500)
  @Convert(converter = FileObjectConverter.class)
  private FileObject media;
  /** 地址 */
  @Column(name = "URL", length = 100)
  private String url;
  /** 按钮文字 */
  @Column(name = "BUTTON_TEXT", length = 100)
  private String buttonText;
}

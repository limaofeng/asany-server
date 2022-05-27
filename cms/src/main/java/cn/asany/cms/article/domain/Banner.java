package cn.asany.cms.article.domain;

import cn.asany.cms.article.domain.enums.BackgroundType;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

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
@Entity
@Table(name = "CMS_BANNER")
@DynamicUpdate
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
  /** 副标题 */
  @Column(name = "SUBTITLE", length = 200)
  private String subtitle;
  /** 描述 */
  @Column(name = "description", length = 500)
  private String description;
  /** 是否启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;
  /** 媒介 */
  @Column(name = "BACKGROUND_TYPE", length = 20)
  @Enumerated(EnumType.STRING)
  private BackgroundType backgroundType;
  /** 媒介 */
  @Column(name = "BACKGROUND", columnDefinition = "TINYTEXT")
  @Convert(converter = FileObjectConverter.class)
  private FileObject background;
  /** 地址 */
  @Column(name = "URL", length = 100)
  private String url;
  /** 按钮文字 */
  @Column(name = "BUTTON_TEXT", length = 100)
  private String buttonText;
}

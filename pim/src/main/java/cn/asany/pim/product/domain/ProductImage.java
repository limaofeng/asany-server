package cn.asany.pim.product.domain;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.*;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "PIM_PRODUCT_IMAGE")
public class ProductImage implements Serializable {

  @Id
  @Column(name = "ID", nullable = false)
  @TableGenerator
  private Long id;

  /** 图片地址 */
  @Column(name = "URL", length = 250)
  private String url;

  /** 本地图片文件 */
  @Column(name = "IMAGE_STORE", columnDefinition = "JSON")
  @Convert(converter = FileObjectConverter.class)
  private FileObject image;

  /** 图片的替代文本 */
  @Column(name = "ALT", length = 250)
  private String alt;

  /** 图片的标题 */
  @Column(name = "TITLE", length = 250)
  private String title;

  /** 图片描述 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;

  /** 排序 */
  @Column(name = "SORT", nullable = false)
  private Integer index;

  /** 产品 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PRODUCT_ID", foreignKey = @ForeignKey(name = "FK_PRODUCT_IMAGE_PID"))
  private Product product;
}

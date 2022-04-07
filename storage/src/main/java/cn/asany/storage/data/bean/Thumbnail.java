package cn.asany.storage.data.bean;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(
    name = "STORAGE_THUMBNAIL",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_STORAGE_THUMBNAIL_SIZE",
          columnNames = {"SOURCE_ID", "SIZE"})
    })
@AllArgsConstructor
@Builder
public class Thumbnail extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 比例 */
  @Column(name = "SIZE", length = 100, nullable = false)
  private String size;
  /** 原文件 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "SOURCE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_STORAGE_THUMBNAIL_SOURCE_ID"))
  @ToString.Exclude
  private FileDetail source;
  /** 缩略图 */
  @OneToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @JoinColumn(
      name = "FILE_ID",
      referencedColumnName = "ID",
      foreignKey = @ForeignKey(name = "FK_STORAGE_THUMBNAIL_FILE_ID"))
  @ToString.Exclude
  private FileDetail file;
}

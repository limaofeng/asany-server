package cn.asany.storage.data.bean;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "STORAGE_FILEOBJECT_LABEL")
public class FileLabel extends BaseBusEntity {

  public static String USER_NAMESPACE = "#private";

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Builder.Default
  @Column(name = "NAME_SPACE", nullable = false, length = 500)
  private String namespace = USER_NAMESPACE;

  @Basic(optional = false)
  @Column(name = "NAME", nullable = false, length = 500)
  private String name;

  @Column(name = "VALUE", nullable = false)
  private String value;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "FILE_ID", foreignKey = @ForeignKey(name = "FK_STORAGE_FILEOBJECT_LABEL_FID"))
  private FileDetail file;
}

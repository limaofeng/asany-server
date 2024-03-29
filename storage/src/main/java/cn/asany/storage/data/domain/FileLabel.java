package cn.asany.storage.data.domain;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;
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

  public static String SYSTEM_NAMESPACE = "#system";
  public static String USER_NAMESPACE = "#private";

  public static final FileLabelBuilder RECYCLE_BIN =
      FileLabel.builder()
          .namespace(SYSTEM_NAMESPACE)
          .name(FileDetail.NAME_OF_THE_RECYCLE_BIN)
          .value("true");
  public static final FileLabelBuilder TEMP_FOLDER =
      FileLabel.builder()
          .namespace(SYSTEM_NAMESPACE)
          .name(FileDetail.NAME_OF_THE_TEMP_FOLDER)
          .value("true");

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
  @JoinColumn(
      name = "FILE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_STORAGE_FILEOBJECT_LABEL_FID"))
  private FileDetail file;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    FileLabel label = (FileLabel) o;
    return id != null && Objects.equals(id, label.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}

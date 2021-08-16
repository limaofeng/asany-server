package cn.asany.ui.library.bean;

import cn.asany.ui.library.bean.enums.Operation;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "SYS_OPLOG")
public class Oplog extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Column(name = "CLAZZ", length = 200)
  private String clazz;

  @Column(name = "ENTITY_NAME", length = 100)
  private String entityName;

  @Column(name = "TABLE_NAME", length = 100)
  private String tableName;

  @Column(name = "PRIMARY_KEY_NAME", length = 100)
  private String primarykeyName;

  @Column(name = "PRIMARY_KEY_VALUE", length = 100)
  private String primarykeyValue;

  @Enumerated(EnumType.STRING)
  @Column(name = "OPERATION", length = 20, nullable = false)
  private Operation operation;

  @Column(name = "data", columnDefinition = "TEXT")
  private String data;

  @ElementCollection
  @CollectionTable(
      name = "SYS_OPLOG_OWNER",
      foreignKey = @ForeignKey(name = "FK_OPLOG_SCOPE"),
      joinColumns = @JoinColumn(name = "LOG_ID"))
  @Column(name = "OWNER")
  private List<String> owners;
}

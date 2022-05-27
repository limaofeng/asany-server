package cn.asany.shanhai.gateway.domain;

import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/** @author limaofeng */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "SH_SERVICE_SCHEMA_VERSION")
public class ServiceSchemaVersion extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Column(name = "NAME", length = 100)
  private String name;

  @Column(name = "MD5", length = 50)
  private String md5;

  @Basic(fetch = FetchType.LAZY)
  @Column(name = "`SCHEMA`", columnDefinition = "mediumtext")
  private String body;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SCHEMA_ID", foreignKey = @ForeignKey(name = "FK_SERVICE_SCHEMA_SID"))
  private ServiceSchema schema;

  @OneToMany(
      mappedBy = "schemaVersion",
      cascade = {CascadeType.REMOVE},
      fetch = FetchType.LAZY)
  private List<ServiceSchemaVersionPatch> patches;
}

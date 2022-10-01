package cn.asany.shanhai.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "SH_MODEL_METADATA")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ModelMetadata implements Serializable {

  @Id
  @Column(name = "MODEL_ID", nullable = false, updatable = false, precision = 22, scale = 0)
  @GenericGenerator(
      name = "ModelMetadataPkGenerator",
      strategy = "foreign",
      parameters = {@Parameter(name = "property", value = "model")})
  @GeneratedValue(generator = "ModelMetadataPkGenerator")
  private Long id;
  /** 数据库名称 */
  @Column(name = "DATABASE_TABLE_NAME", length = 100)
  private String databaseTableName;

  /** Hibernate Mapping XML */
  @Column(name = "HIBERNATE_MAPPING", columnDefinition = "Text")
  private String hbm;

  @OneToOne(fetch = FetchType.LAZY)
  @PrimaryKeyJoinColumn
  @ToString.Exclude
  private Model model;
}

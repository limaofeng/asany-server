package cn.asany.shanhai.core.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "SH_MODEL_FIELD_METADATA")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ModelFieldMetadata implements Serializable {
    @Id
    @Column(name = "FIELD_ID", nullable = false, updatable = false, precision = 22, scale = 0)
    @GenericGenerator(name = "pkGenerator", strategy = "foreign", parameters = {@Parameter(name = "property", value = "field")})
    @GeneratedValue(generator = "pkGenerator")
    private Long id;
    /**
     * 实体名称，用于 HQL 名称及 API 名称
     */
    @Column(name = "NAME", length = 100)
    private String name;
    /**
     * 数据库中的列表名称
     */
    @Column(name = "DATABASE_COLUMN_NAME", length = 100)
    private String databaseColumnName;

    /**
     * 是否唯一
     */
    @Column(name = "IS_UNIQUE", length = 1)
    private Boolean unique;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @PrimaryKeyJoinColumn
    private ModelField field;
}

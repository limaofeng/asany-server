package cn.asany.shanhai.core.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(of = "id")
@Entity
@Table(name = "SH_MODEL_METADATA")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ModelMetadata implements Serializable {

    @Id
    @Column(name = "MODEL_ID", nullable = false, updatable = false, precision = 22, scale = 0)
    @GenericGenerator(name = "ModelMetadataPkGenerator", strategy = "foreign", parameters = {@Parameter(name = "property", value = "model")})
    @GeneratedValue(generator = "ModelMetadataPkGenerator")
    private Long id;
    /**
     * 数据库名称
     */
    @Column(name = "DATABASE_TABLE_NAME", length = 100)
    private String databaseTableName;

    /**
     * Hibernate Mapping XML
     */
    @Column(name = "HIBERNATE_MAPPING", columnDefinition = "Text")
    private String hbm;

    @LazyToOne(LazyToOneOption.NO_PROXY)
    @OneToOne(fetch = FetchType.LAZY, targetEntity = Model.class, mappedBy = "metadata")
    private Model model;
}
package cn.asany.shanhai.gateway.bean;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.List;

/**
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "SH_SERVICE_SCHEMA")
public class ServiceSchema extends BaseBusEntity {

    @Id
    @Column(name = "SERVICE_ID", nullable = false, updatable = false, precision = 22, scale = 0)
    @GenericGenerator(name = "ModelMetadataPkGenerator", strategy = "foreign", parameters = {@org.hibernate.annotations.Parameter(name = "property", value = "service")})
    @GeneratedValue(generator = "ModelMetadataPkGenerator")
    private Long id;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "`SCHEMA`", columnDefinition = "mediumtext")
    private String schema;

    @LazyToOne(LazyToOneOption.NO_PROXY)
    @OneToOne(fetch = FetchType.LAZY, targetEntity = Service.class, mappedBy = "schema")
    private Service service;

    @OrderBy("createdAt desc")
    @OneToMany(mappedBy = "schema", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<ServiceSchemaVersion> versions;

    @Transient
    public ServiceSchemaVersion latest() {
        if (this.getVersions().isEmpty()) {
            return null;
        }
        return this.getVersions().get(0);
    }
}

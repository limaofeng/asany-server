package cn.asany.shanhai.gateway.bean;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "SH_SERVICE_SCHEMA_VERSION_PATCH")
public class ServiceSchemaVersionPatch extends BaseBusEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEMA_VERSION_ID", foreignKey = @ForeignKey(name = "FK_SERVICE_SCHEMA_VERSION_PATCH_SID"))
    private ServiceSchemaVersion schemaVersion;

}

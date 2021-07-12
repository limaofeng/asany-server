package cn.asany.shanhai.gateway.bean;

import cn.asany.shanhai.gateway.bean.enums.ServiceSchemaVersionPatchStatus;
import cn.asany.shanhai.gateway.bean.enums.ServiceSchemaVersionPatchType;
import cn.asany.shanhai.gateway.util.DiffObject;
import cn.asany.shanhai.gateway.util.GraphQLType;
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

    @Column(name = "NAME", length = 100)
    private String name;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 50)
    private ServiceSchemaVersionPatchType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 50)
    private ServiceSchemaVersionPatchStatus status;

    @Builder.Default
    @Column(name = "INSTALLED", length = 50)
    private Boolean installed = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEMA_VERSION_ID", foreignKey = @ForeignKey(name = "FK_SERVICE_SCHEMA_VERSION_PATCH_SID"))
    private ServiceSchemaVersion schemaVersion;

    public static class ServiceSchemaVersionPatchBuilder {
        public ServiceSchemaVersionPatchBuilder type(GraphQLType type) {
            return this;
        }

        public ServiceSchemaVersionPatchBuilder status(DiffObject.DiffStatus type) {
            return this;
        }

    }


}
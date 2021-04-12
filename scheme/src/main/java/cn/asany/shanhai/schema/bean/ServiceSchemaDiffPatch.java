package cn.asany.shanhai.schema.bean;

import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "SH_SERVICE_SCHEMA_DIFF_PATCH")
public class ServiceSchemaDiffPatch extends BaseBusEntity {

    private ServiceSchema schema;

}

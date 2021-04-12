package cn.asany.shanhai.schema.bean;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "SH_SERVICE_SCHEMA")
public class ServiceSchema extends BaseBusEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    @Column(name = "SCHEMA", columnDefinition = "text")
    private String schema;

    private List<ServiceSchemaDiffPatch> patches;

}

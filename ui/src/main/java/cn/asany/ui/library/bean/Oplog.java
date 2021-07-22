package cn.asany.ui.library.bean;

import cn.asany.ui.library.bean.enums.Operation;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

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
    @Column(name = "ENTITY_NAME", length = 100)
    private String entityName;
    @Column(name = "TABLE_NAME", length = 100)
    private String tableName;
    @Enumerated(EnumType.STRING)
    @Column(name = "OPERATION", length = 20, nullable = false)
    private Operation operation;
    @Column(name = "data_before", columnDefinition = "TEXT")
    private String dataBefore;
    @Column(name = "data_after", columnDefinition = "TEXT")
    private String dataAfter;

}

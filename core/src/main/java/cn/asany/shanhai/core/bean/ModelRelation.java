package cn.asany.shanhai.core.bean;


import cn.asany.shanhai.core.bean.enums.ModelRelationType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * 模型关系
 * 记录模型与其他模型直接的关联关系
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "SH_MODEL_RELATION")
public class ModelRelation extends BaseBusEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 关系类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 20)
    private ModelRelationType type;
    /**
     * 关系类型
     * SUBJECTION  INPUT / SLAVE
     */
    @Column(name = "RELATION", length = 20)
    private String relation;
    /**
     * 当前实体
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODEL_ID", foreignKey = @ForeignKey(name = "FK_SH_MODEL_FIELD_MODEL_ID"), nullable = false)
    private Model model;
    /**
     * 关联实体
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INVERSE", foreignKey = @ForeignKey(name = "FK_MODEL_RELATION_INVERSE"), nullable = false)
    private Model inverse;

}

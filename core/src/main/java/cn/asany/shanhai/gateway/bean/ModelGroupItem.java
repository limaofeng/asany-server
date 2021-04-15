package cn.asany.shanhai.gateway.bean;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "SH_MODEL_GROUP_ITEM")
public class ModelGroupItem extends BaseBusEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 引用资源
     */
    @Column(name = "RESOURCE", length = 100)
    private String resource;
    /**
     * 排序
     */
    @Column(name = "SORT")
    private Integer sort;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID", foreignKey = @ForeignKey(name = "FK_MODEL_GROUP_ITEM_GID"), nullable = false)
    private ModelGroup group;
}
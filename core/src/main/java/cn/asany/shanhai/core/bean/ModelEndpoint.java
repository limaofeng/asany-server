package cn.asany.shanhai.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@Table(name = "SH_MODEL_ENDPOINT")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ModelEndpoint extends BaseBusEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 100)
    private String name;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 200)
    private String description;
    /**
     * 实体
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODEL_ID", foreignKey = @ForeignKey(name = "FK_SH_MODEL_ENDPOINT_MID"), nullable = false)
    private Model model;
    /**
     * 参数
     */
    @Transient
    private String arguments;
}

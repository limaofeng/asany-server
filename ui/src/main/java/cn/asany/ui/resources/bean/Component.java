package cn.asany.ui.resources.bean;

import cn.asany.ui.resources.bean.converter.ComponentDataConverter;
import cn.asany.ui.resources.bean.enums.ComponentScope;
import cn.asany.ui.resources.bean.enums.ComponentType;
import cn.asany.ui.resources.bean.toy.ComponentData;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.List;

/**
 * 组件
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "NUWA_COMPONENT")
public class Component extends BaseBusEntity {

    public static final String RESOURCE_NAME = "COMPONENT";

    @Id
    @Column(name = "ID", length = 50, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 使用范围
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "SCOPE", length = 50, nullable = false)
    private ComponentScope scope;
    /**
     * 组件类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 50)
    private ComponentType type;
    /**
     * 名称
     */
    @Column(name = "CODE")
    private String code;
    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 组件模版
     */
    @Column(name = "TEMPLATE")
    private String template;
    /**
     * 组件数据
     */
    @Convert(converter = ComponentDataConverter.class)
    @Column(name = "PROPS", columnDefinition = "JSON")
    private List<ComponentData> props;
}

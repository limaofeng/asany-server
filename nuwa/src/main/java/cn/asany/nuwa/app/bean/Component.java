package cn.asany.nuwa.app.bean;

import cn.asany.nuwa.app.bean.enums.ComponentType;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

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

    @Id
    @Column(name = "ID", length = 50, updatable = false)
    private Long id;
    /**
     * 组件类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 50)
    private ComponentType type;
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
    @Column(name = "PROPS", columnDefinition = "JSON")
    private String props;
}

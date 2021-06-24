package cn.asany.ui.resources.bean;

import cn.asany.ui.resources.UIResource;
import cn.asany.ui.resources.bean.enums.IconType;
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
@Table(name = "UI_ICON")
public class Icon extends BaseBusEntity implements UIResource {

    public static final String RESOURCE_NAME = "ICON";
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 20, nullable = false)
    private IconType type;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 60)
    private String name;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION")
    private String description;
    /**
     * 内容
     */
    @Column(name = "CONTENT", columnDefinition = "TEXT")
    private String content;
}

package cn.asany.nuwa.app.bean;

import cn.asany.nuwa.app.bean.enums.RouteType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.StringSetConverter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * 路由
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "NUWA_APPLICATION_ROUTE")
public class ApplicationRoute extends BaseBusEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 菜单名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 路由所属类型 PC端/M站
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPACE", foreignKey = @ForeignKey(name = "FK_ROUTE_SPACE"), updatable = false, nullable = false)
    private Routespace space;
    /**
     * 路由类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private RouteType type;
    /**
     * 层级
     */
    @Column(name = "LEVEL")
    private String level;
    /**
     * 路径
     */
    @Column(name = "PATH")
    private String path;
    /**
     * 重定向
     */
    @Column(name = "REDIRECT")
    private String redirect;
    /**
     * 组件
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "COMPONENT_ID", foreignKey = @ForeignKey(name = "FK_ROUTE_COMPONENT"))
    private Component component;
    /**
     * 对应的图标
     */
    @Column(name = "ICON")
    private String icon;

    /**
     * 父路由
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "PID", foreignKey = @ForeignKey(name = "FK_ROUTE_PID"))
    private ApplicationRoute parent;
    /**
     * 子路由
     */
    @JsonInclude(content = JsonInclude.Include.NON_NULL)
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("sort ASC")
    private List<ApplicationRoute> routes;
    /**
     * 可以访问的权限
     */
    @Convert(converter = StringSetConverter.class)
    @Column(name = "AUTHORITY")
    private Set<String> authority;
    /**
     * 必须授权才能访问
     */
    @Column(name = "AUTHORIZED")
    private Boolean authorized;
    /**
     * 是否启用
     */
    @Column(name = "ENABLED")
    private Boolean enabled;
    /**
     * 不在菜单中显示子菜单
     */
    @Column(name = "HIDE_CHILDREN_IN_MENU")
    private Boolean hideChildrenInMenu;
    /**
     * 在面包屑中隐藏菜单
     */
    @Column(name = "HIDE_IN_BREADCRUMB")
    private Boolean hideInBreadcrumb;
    /**
     * 不在菜单中显示
     */
    @Column(name = "HIDE_IN_MENU")
    private Boolean hideInMenu;
    /**
     * 应用
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APPLICATION_ID", foreignKey = @ForeignKey(name = "FK_ROUTE_APPID"), updatable = false, nullable = false)
    private Application application;
    /**
     * 序号
     */
    @Column(name = "SORT")
    private Long index;

}

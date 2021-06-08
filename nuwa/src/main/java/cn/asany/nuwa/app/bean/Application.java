package cn.asany.nuwa.app.bean;

import cn.asany.organization.core.bean.Organization;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.List;

/**
 * 应用信息
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "NUWA_APPLICATION")
public class Application extends BaseBusEntity {
    /**
     * ID
     */
    @Id
    @Column(name = "ID", length = 50, updatable = false)
    private String id;
    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 应用访问地址
     */
    @Column(name = "URL")
    private String url;
    /**
     * 挂载点
     */
    @Column(name = "MOUNT_POINT")
    private String mountPoint;
    /**
     * 应用根路径
     */
    @Column(name = "PATH")
    private String path;
    /**
     * 是否启用
     */
    @Column(name = "ENABLED")
    private Boolean enabled;
    /**
     * 简介
     */
    @Column(name = "DESCRIPTION")
    private String description;
    /**
     * LOGO
     */
    @Column(name = "LOGO")
    private String logo;
    /**
     * 组织
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGANIZATION_ID", foreignKey = @ForeignKey(name = "FK_APPLICATION_ORGANIZATION"), updatable = false, nullable = false)
    private Organization organization;
    /**
     * 路由
     */
    @OneToMany(mappedBy = "application", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Route> routes;
}

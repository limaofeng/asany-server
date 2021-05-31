package cn.asany.security.core.bean;

import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.List;

/**
 * @author: guoyong
 * @description: 权限分类表
 * @create: 2020/6/9 11:18
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AUTH_PERMISSION_TYPE")
public class PermissionType extends BaseBusEntity {

    private static final long serialVersionUID = 2224908963065749111L;

    /**
     * 未知分类存值
     */
    public static final String UNKNOWN = "UNKNOWN";

    /**
     * 权限分类编码
     */
    @Id
    @Column(name = "ID", length = 32)
    private String id;
    /**
     * 权限分类名称
     */
    @Column(name = "NAME", length = 50)
    private String name;

    /**
     * 描述信息
     */
    @Column(name = "DESCRIPTION", length = 250)
    private String description;

    /**
     * 排序
     */
    @Column(name = "SORT", length = 11)
    private int sort;

    /**
     * 对应的权限定义
     */
    @OneToMany(mappedBy = "permissionType", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Permission> permissions;
}

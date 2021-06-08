package cn.asany.nuwa.app.graphql.input;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建应用的表单
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
public class ApplicationCreateInput {
    /**
     * 名称
     */
    private String name;
    /**
     * 简介
     */
    private String description;
    /**
     * 应用 LOGO
     */
    private String logo;
    /**
     * 封面图
     */
    private String cover;
    /**
     * 组织
     */
    private String organization;
    /**
     * 应用根路径
     */
    private String path;
    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 集成钉钉
     */
    private Boolean dingtalkIntegration;
}

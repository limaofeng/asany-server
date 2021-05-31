package cn.asany.security.auth.graphql.types;

import cn.asany.security.core.bean.enums.SocialProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author limaofeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginOptions {
    /**
     * 行为说明：
     * 1. LoginType = 钉钉 登录时可用,
     * 设置为 true 时, 将自动关联已有账户 (通过手机号)
     * 2. 用户密码登录，需要额外设置
     *  provider = ding
     *  snser = 钉钉 id
     */
    @Builder.Default
    private Boolean connected = Boolean.FALSE;
    private SocialProvider provider;
    private String snser;
}

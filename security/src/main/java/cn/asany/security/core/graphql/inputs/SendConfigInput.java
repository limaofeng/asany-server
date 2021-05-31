package cn.asany.security.core.graphql.inputs;

import lombok.Builder;
import lombok.Data;
import cn.asany.security.core.bean.enums.PsdSendType;
import java.io.Serializable;

/**
 * @description:
 * @author: Poison
 * @time: 2020/9/22 3:14 PM
 */
@Data
@Builder
public class SendConfigInput implements Serializable {

    /**
     * 类型
     */
    private PsdSendType type;
    /**
     * 模版ID
     */
    private String template;
    /**
     * 发件箱
     */
    private String outbox;
    /**
     * 到期时间
     */
    private Long effectiveDuration;
    /**
     * 跳转路径
     */
    private String path;
    /**
     * 是否禁用
     */
    private Boolean disable;
    /**
     * 流程key
     */
    private String processKey;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 验证码的长度
     */
    private Integer wordLength;
    /**
     * 验证码随机字符串
     */
    private String randomWord;
    /**
     * 验证码重复生成时间
     */
    private Integer active;
    /**
     * 验证码验证重试次数
     */
    private Integer retry;
}

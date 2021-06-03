package cn.asany.security.oauth.vo;

import lombok.Data;

import java.util.Date;

/**
 * 会话
 *
 * @author limaofeng
 */
@Data
public class SessionAccessToken {
    /**
     * ID
     */
    private Long id;
    /**
     * 设备
     */
    private String device;
    /**
     * 位置信息
     */
    private Geolocation location;
    /**
     * 登录时间
     */
    private Date loginTime;

}

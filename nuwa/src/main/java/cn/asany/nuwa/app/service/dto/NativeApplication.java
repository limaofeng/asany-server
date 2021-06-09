package cn.asany.nuwa.app.service.dto;

import lombok.Data;

import java.util.Set;

/**
 * 原生应用
 *
 * @author limaofeng
 */
@Data
public class NativeApplication {
    private String name;
    private String url;
    private String description;
    private String callbackUrl;
    private String setupURL;
    private WebHook webhook;
    private Set<String> authorities;
}

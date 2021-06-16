package cn.asany.nuwa.app.service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 原生应用
 *
 * @author limaofeng
 */
@Data
@Builder
public class NativeApplication {
    private String name;
    private String url;
    private String description;
    private String callbackUrl;
    private String setupURL;
    private WebHook webhook;
    private Set<String> routespaces;
    private Set<String> authorities;

    public static class NativeApplicationBuilder {

        public NativeApplicationBuilder routespaces(String... spaces) {
            this.routespaces = new HashSet<>(Arrays.asList(spaces));
            return this;
        }
    }
}

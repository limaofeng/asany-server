package org.jfantasy.framework.security.oauth2.jwt;

import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.security.oauth2.JwtTokenPayload;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.util.Base64Utils;

public class JwtUtils {

    public static JwtTokenPayload payload(String accessToken) {
        String[] strings = StringUtil.tokenizeToStringArray(accessToken, ".");
        return JSON.deserialize(new String(Base64Utils.decodeFromString(strings[1])), JwtTokenPayload.class);
    }

}

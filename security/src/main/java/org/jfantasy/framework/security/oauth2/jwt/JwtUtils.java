package org.jfantasy.framework.security.oauth2.jwt;

import org.apache.commons.codec.binary.Base64;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.security.oauth2.JwtTokenPayload;
import org.jfantasy.framework.util.common.StringUtil;

public class JwtUtils {

    public static JwtTokenPayload payload(String accessToken) {
        String[] strings = StringUtil.tokenizeToStringArray(accessToken, ".");
        return JSON.deserialize(new String(Base64.decodeBase64(strings[1])), JwtTokenPayload.class);
    }

}

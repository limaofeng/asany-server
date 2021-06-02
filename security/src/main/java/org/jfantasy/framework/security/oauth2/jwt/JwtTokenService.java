package org.jfantasy.framework.security.oauth2.jwt;

import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface JwtTokenService {

    String generateToken(String payloadStr, String secret) throws JOSEException;

    String verifyToken(String token, String secret) throws ParseException, JOSEException;

}

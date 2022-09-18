package com.velox.server.security;

import com.velox.server.security.jwt.JwtUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class SecureCommands {

    private static final Logger logger = LoggerFactory.getLogger(SecureCommands.class);

    @Value("${velox.app.jwtSecret}")
    private String jwtSecret;


    public String generateTokenFromCommand(String command) {
        return Jwts.builder().setSubject(command).signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getCommandFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
}

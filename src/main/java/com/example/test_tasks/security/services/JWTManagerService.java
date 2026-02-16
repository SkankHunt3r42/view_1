package com.example.test_tasks.security.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.test_tasks.entites.enums.AllowedProviders;
import jakarta.servlet.http.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JWTManagerService {

    public final Algorithm algorithm = Algorithm.HMAC256("${S_K}");

    public String[] generateTokenPairs(String email, AllowedProviders provider){

        String accessToken = JWT.create()
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusSeconds(300))
                .withIssuer(provider.name())
                .withSubject(email)
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusSeconds(3600))
                .withSubject(email)
                .sign(algorithm);

        return new String[]{accessToken,refreshToken};
    }

    public HttpHeaders getTokensHeadersWrapped(String[] arr){

        HttpHeaders headers = new HttpHeaders();

        ResponseCookie access = ResponseCookie.from("Access",arr[0])
                .httpOnly(true)
                .maxAge(300)
                .sameSite("Lax")
                .build();

        ResponseCookie refresh = ResponseCookie.from("Refresh",arr[1])
                .httpOnly(true)
                .maxAge(3600)
                .sameSite("Lax")
                .path("/refresh")
                .build();

        headers.add(HttpHeaders.SET_COOKIE,access.toString());
        headers.add(HttpHeaders.SET_COOKIE,refresh.toString());

        return headers;
    }

    public ResponseCookie[] getTokensCookieWrapped(String[] arr){


        ResponseCookie access = ResponseCookie.from("Access",arr[0])
                .httpOnly(true)
                .maxAge(300)
                .build();

        ResponseCookie refresh = ResponseCookie.from("Refresh",arr[1])
                .httpOnly(true)
                .maxAge(3600)
                .path("/refresh")
                .build();

        return new ResponseCookie[]{access,refresh};
    }

    public DecodedJWT plainJwtDecoder(String token){

        JWTVerifier verifier = JWT
                .require(algorithm)
                .withIssuer(AllowedProviders.GOOGLE.name(),AllowedProviders.LOCAL.name())
                .build();

        return verifier.verify(token);
    }


}

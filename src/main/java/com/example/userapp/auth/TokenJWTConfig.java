package com.example.userapp.auth;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class TokenJWTConfig {
   public static final String CONTENT_TYPE = "application/json";

   public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
   public static final String PREFIX = "Bearer ";
   public static final String HEADER_STRING = "Authorization";
}

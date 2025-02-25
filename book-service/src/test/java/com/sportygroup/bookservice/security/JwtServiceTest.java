package com.sportygroup.bookservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    public static final String TEST_USER = "testUser";
    public static final String SECRET = "my-super-secret-key-which-is-very" +
            "-secure";

    @Mock
    private UserDetails userDetails;

    private JwtService jwtService;

    private String testToken;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET);
        testToken = generateTestToken(TEST_USER);
    }

    @Test
    void shouldExtractUsername() {
        String username = jwtService.extractUsername(testToken);
        assertEquals(TEST_USER, username);
    }

    @Test
    void shouldValidateToken() {
        when(userDetails.getUsername()).thenReturn(TEST_USER);
        assertTrue(jwtService.isTokenValid(testToken, userDetails));
    }

    @Test
    void shouldInvalidateTokenWhenUsernameDoesNotMatch() {
        when(userDetails.getUsername()).thenReturn("wrongUser");
        assertFalse(jwtService.isTokenValid(testToken, userDetails));
    }

    private String generateTestToken(String username) {
        Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
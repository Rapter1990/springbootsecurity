package com.springboot.springbootsecurity.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.springbootsecurity.admin.model.Admin;
import com.springboot.springbootsecurity.admin.model.dto.request.AdminRegisterRequest;
import com.springboot.springbootsecurity.admin.service.AdminLoginService;
import com.springboot.springbootsecurity.admin.service.AdminLogoutService;
import com.springboot.springbootsecurity.admin.service.AdminRefreshTokenService;
import com.springboot.springbootsecurity.admin.service.AdminRegisterService;
import com.springboot.springbootsecurity.auth.model.Token;
import com.springboot.springbootsecurity.auth.model.dto.request.LoginRequest;
import com.springboot.springbootsecurity.auth.model.dto.request.TokenInvalidateRequest;
import com.springboot.springbootsecurity.auth.model.dto.request.TokenRefreshRequest;
import com.springboot.springbootsecurity.auth.model.dto.response.TokenResponse;
import com.springboot.springbootsecurity.auth.model.mapper.TokenToTokenResponseMapper;
import com.springboot.springbootsecurity.base.AbstractRestControllerTest;
import com.springboot.springbootsecurity.common.model.dto.response.CustomResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminAuthControllerTest extends AbstractRestControllerTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private AdminRegisterService adminRegisterService;

    @MockBean
    private AdminLoginService adminLoginService;

    @MockBean
    private AdminRefreshTokenService adminRefreshTokenService;

    @MockBean
    private AdminLogoutService adminLogoutService;

    private TokenToTokenResponseMapper tokenToTokenResponseMapper = TokenToTokenResponseMapper.initialize();


    @Test
    void givenValidAdminRegisterRequest_whenRegisterAdmin_thenSuccess() throws Exception {

        // Given
        AdminRegisterRequest adminRegisterRequest = AdminRegisterRequest.builder()
                .email("admin@example.com")
                .password("password")
                .firstName("Admin")
                .lastName("User")
                .phoneNumber("12345678910")
                .build();

        Admin mockAdmin = Admin.builder()
                .id(UUID.randomUUID().toString())
                .email(adminRegisterRequest.getEmail())
                .firstName(adminRegisterRequest.getFirstName())
                .lastName(adminRegisterRequest.getLastName())
                .phoneNumber(adminRegisterRequest.getPhoneNumber())
                .password(adminRegisterRequest.getPassword())
                .build();

        // When
        when(adminRegisterService.registerAdmin(any(AdminRegisterRequest.class))).thenReturn(mockAdmin);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authentication/admin/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminRegisterRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(CustomResponse.SUCCESS)));

        // Verify
        verify(adminRegisterService, times(1)).registerAdmin(any(AdminRegisterRequest.class));

    }

    @Test
    void loginAdmin() throws Exception {

        // Given
        LoginRequest loginRequest = LoginRequest.builder()
                .email("admin@example.com")
                .password("password")
                .build();

        Token mockToken = Token.builder()
                .accessToken("mockAccessToken")
                .accessTokenExpiresAt(3600L)
                .refreshToken("mockRefreshToken")
                .build();

        TokenResponse expectedTokenResponse = tokenToTokenResponseMapper.map(mockToken);

        // When
        when(adminLoginService.login(any(LoginRequest.class))).thenReturn(mockToken);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authentication/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessToken").value(expectedTokenResponse.getAccessToken()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessTokenExpiresAt").value(expectedTokenResponse.getAccessTokenExpiresAt()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.refreshToken").value(expectedTokenResponse.getRefreshToken()));

        // Verify
        verify(adminLoginService, times(1)).login(any(LoginRequest.class));

    }


    @Test
    void refreshToken() throws Exception {

        // Given
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest("refreshToken");

        Token mockToken = Token.builder()
                .accessToken("mockAccessToken")
                .accessTokenExpiresAt(3600L)
                .refreshToken("mockRefreshToken")
                .build();

        TokenResponse expectedTokenResponse = tokenToTokenResponseMapper.map(mockToken);

        // When
        when(adminRefreshTokenService.refreshToken(any(TokenRefreshRequest.class))).thenReturn(mockToken);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authentication/admin/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenRefreshRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessToken").value(expectedTokenResponse.getAccessToken()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessTokenExpiresAt").value(expectedTokenResponse.getAccessTokenExpiresAt()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.refreshToken").value(expectedTokenResponse.getRefreshToken()));

        // Verify
        verify(adminRefreshTokenService, times(1)).refreshToken(any(TokenRefreshRequest.class));

    }


    @Test
    void logout() throws Exception {

        // Given
        TokenInvalidateRequest tokenInvalidateRequest = TokenInvalidateRequest.builder()
                .accessToken("mockAccessToken")
                .refreshToken("mockRefreshToken")
                .build();

        // When
        doNothing().when(adminLogoutService).logout(any(TokenInvalidateRequest.class));

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authentication/admin/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, mockAdminToken)
                        .content(objectMapper.writeValueAsString(tokenInvalidateRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(CustomResponse.SUCCESS)));

        // Verify
        verify(adminLogoutService, times(1)).logout(any(TokenInvalidateRequest.class));

    }

}
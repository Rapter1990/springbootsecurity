package com.springboot.springbootsecurity.admin.service.impl;

import com.springboot.springbootsecurity.admin.exception.AdminNotFoundException;
import com.springboot.springbootsecurity.admin.model.entity.AdminEntity;
import com.springboot.springbootsecurity.admin.repository.AdminRepository;
import com.springboot.springbootsecurity.auth.exception.PasswordNotValidException;
import com.springboot.springbootsecurity.auth.model.Token;
import com.springboot.springbootsecurity.auth.model.dto.request.LoginRequest;
import com.springboot.springbootsecurity.auth.service.TokenService;
import com.springboot.springbootsecurity.base.AbstractBaseServiceTest;
import com.springboot.springbootsecurity.builder.AdminEntityBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminLoginServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private AdminLoginServiceImpl adminLoginService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Test
    void login_ValidCredentials_ReturnsToken() {

        // Given
        final LoginRequest loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        final AdminEntity adminEntity = new AdminEntityBuilder().withValidFields().build();

        final Token expectedToken = Token.builder()
                .accessToken("mockAccessToken")
                .accessTokenExpiresAt(123456789L)
                .refreshToken("mockRefreshToken")
                .build();

        // When
        when(adminRepository.findAdminEntityByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(adminEntity));

        when(passwordEncoder.matches(loginRequest.getPassword(), adminEntity.getPassword()))
                .thenReturn(true);

        when(tokenService.generateToken(adminEntity.getClaims())).thenReturn(expectedToken);

        Token actualToken = adminLoginService.login(loginRequest);

        // Then
        assertEquals(expectedToken.getAccessToken(), actualToken.getAccessToken());
        assertEquals(expectedToken.getRefreshToken(), actualToken.getRefreshToken());
        assertEquals(expectedToken.getAccessTokenExpiresAt(), actualToken.getAccessTokenExpiresAt());

        // Verify
        verify(adminRepository).findAdminEntityByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), adminEntity.getPassword());
        verify(tokenService).generateToken(adminEntity.getClaims());

    }

    @Test
    void login_InvalidEmail_ThrowsAdminNotFoundException() {

        // Given
        final LoginRequest loginRequest = LoginRequest.builder()
                .email("nonexistent@example.com")
                .password("password123")
                .build();

        // When
        when(adminRepository.findAdminEntityByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.empty());

        // Then
        AdminNotFoundException exception = assertThrows(AdminNotFoundException.class,
                () -> adminLoginService.login(loginRequest));

        assertEquals("Admin not found!\n Can't find with given email: " + loginRequest.getEmail(), exception.getMessage());

        // Verify
        verify(adminRepository).findAdminEntityByEmail(loginRequest.getEmail());
        verifyNoInteractions(passwordEncoder, tokenService);

    }

    @Test
    void login_InvalidPassword_ThrowsPasswordNotValidException() {

        // Given
        final LoginRequest loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("invalidPassword")
                .build();

        final AdminEntity adminEntity = AdminEntity.builder()
                .email(loginRequest.getEmail())
                .password("encodedPassword")
                .build();

        // When
        when(adminRepository.findAdminEntityByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(adminEntity));

        when(passwordEncoder.matches(loginRequest.getPassword(), adminEntity.getPassword()))
                .thenReturn(false);

        // Then
        PasswordNotValidException exception = assertThrows(PasswordNotValidException.class,
                () -> adminLoginService.login(loginRequest));

        assertNotNull(exception);

        // Verify
        verify(adminRepository).findAdminEntityByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), adminEntity.getPassword());
        verifyNoInteractions(tokenService);

    }

}
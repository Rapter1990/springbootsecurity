package com.springboot.springbootsecurity.admin.service.impl;

import com.springboot.springbootsecurity.admin.model.entity.AdminEntity;
import com.springboot.springbootsecurity.auth.model.dto.request.TokenInvalidateRequest;
import com.springboot.springbootsecurity.auth.service.InvalidTokenService;
import com.springboot.springbootsecurity.auth.service.TokenService;
import com.springboot.springbootsecurity.base.AbstractBaseServiceTest;
import com.springboot.springbootsecurity.builder.AdminEntityBuilder;
import com.springboot.springbootsecurity.builder.TokenBuilder;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Set;

import static org.mockito.Mockito.*;

class AdminLogoutServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private AdminLogoutServiceImpl adminLogoutService;

    @Mock
    private TokenService tokenService;

    @Mock
    private InvalidTokenService invalidTokenService;

    @Test
    void givenAccessTokenAndRefreshToken_whenLogoutForAdmin_thenReturnLogout() {

        // Given
        final AdminEntity mockAdminEntity = new AdminEntityBuilder().withValidFields().build();

        final Claims mockAccessTokenClaims = TokenBuilder.getValidClaims(
                mockAdminEntity.getId(),
                mockAdminEntity.getFirstName()
        );

        final Claims mockRefreshTokenClaims = TokenBuilder.getValidClaims(
                mockAdminEntity.getId(),
                mockAdminEntity.getFirstName()
        );

        final String mockAccessTokenId = mockAccessTokenClaims.getId();
        final String mockRefreshTokenId = mockRefreshTokenClaims.getId();


        final String accessToken = "validAccessToken";
        final String refreshToken = "validRefreshToken";

        final TokenInvalidateRequest tokenInvalidateRequest = TokenInvalidateRequest.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        // When
        doNothing().when(tokenService).verifyAndValidate(Set.of(accessToken, refreshToken));
        when(tokenService.getPayload(accessToken)).thenReturn(mockAccessTokenClaims);
        doNothing().when(invalidTokenService).checkForInvalidityOfToken(mockAccessTokenId);
        when(tokenService.getPayload(refreshToken)).thenReturn(mockRefreshTokenClaims);
        doNothing().when(invalidTokenService).checkForInvalidityOfToken(mockRefreshTokenId);
        doNothing().when(invalidTokenService).invalidateTokens(Set.of(mockAccessTokenId, mockRefreshTokenId));

        // Then
        adminLogoutService.logout(tokenInvalidateRequest);

        // Verify
        verify(tokenService).verifyAndValidate(Set.of(accessToken, refreshToken));
        verify(tokenService, times(2)).getPayload(anyString());
        verify(invalidTokenService, times(2)).checkForInvalidityOfToken(anyString());
        verify(invalidTokenService).invalidateTokens(anySet());

    }


}
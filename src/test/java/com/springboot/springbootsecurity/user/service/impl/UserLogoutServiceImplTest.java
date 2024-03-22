package com.springboot.springbootsecurity.user.service.impl;

import com.springboot.springbootsecurity.auth.model.dto.request.TokenInvalidateRequest;
import com.springboot.springbootsecurity.auth.service.InvalidTokenService;
import com.springboot.springbootsecurity.auth.service.TokenService;
import com.springboot.springbootsecurity.base.AbstractBaseServiceTest;
import com.springboot.springbootsecurity.builder.TokenBuilder;
import com.springboot.springbootsecurity.builder.UserEntityBuilder;
import com.springboot.springbootsecurity.user.model.entity.UserEntity;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Set;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserLogoutServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private UserLogoutServiceImpl userLogoutService;

    @Mock
    private TokenService tokenService;

    @Mock
    private InvalidTokenService invalidTokenService;

    @Test
    void givenAccessTokenAndRefreshToken_whenLogoutForUser_thenReturnLogout() {

        UserEntity mockUserEntity = new UserEntityBuilder().withValidFields().build();

        Claims mockAccessTokenClaims = TokenBuilder.getValidClaims(
                mockUserEntity.getId(),
                mockUserEntity.getFirstName()
        );

        Claims mockRefreshTokenClaims = TokenBuilder.getValidClaims(
                mockUserEntity.getId(),
                mockUserEntity.getFirstName()
        );

        String mockAccessTokenId = mockAccessTokenClaims.getId();
        String mockRefreshTokenId = mockRefreshTokenClaims.getId();

        // Given
        String accessToken = "validAccessToken";
        String refreshToken = "validRefreshToken";

        TokenInvalidateRequest tokenInvalidateRequest = TokenInvalidateRequest.builder()
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
        userLogoutService.logout(tokenInvalidateRequest);

        // Verify
        verify(tokenService).verifyAndValidate(Set.of(accessToken, refreshToken));
        verify(tokenService, times(2)).getPayload(anyString());
        verify(invalidTokenService, times(2)).checkForInvalidityOfToken(anyString());
        verify(invalidTokenService).invalidateTokens(anySet());

    }

}
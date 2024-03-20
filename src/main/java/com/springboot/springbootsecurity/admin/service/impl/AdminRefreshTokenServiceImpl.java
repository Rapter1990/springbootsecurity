package com.springboot.springbootsecurity.admin.service.impl;

import com.springboot.springbootsecurity.admin.exception.AdminNotFoundException;
import com.springboot.springbootsecurity.admin.model.entity.AdminEntity;
import com.springboot.springbootsecurity.admin.repository.AdminRepository;
import com.springboot.springbootsecurity.admin.service.AdminRefreshTokenService;
import com.springboot.springbootsecurity.auth.exception.UserStatusNotValidException;
import com.springboot.springbootsecurity.auth.model.Token;
import com.springboot.springbootsecurity.auth.model.dto.request.TokenRefreshRequest;
import com.springboot.springbootsecurity.auth.model.enums.TokenClaims;
import com.springboot.springbootsecurity.auth.model.enums.UserStatus;
import com.springboot.springbootsecurity.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminRefreshTokenServiceImpl implements AdminRefreshTokenService {

    private final AdminRepository adminRepository;
    private final TokenService tokenService;

    @Override
    public Token refreshToken(TokenRefreshRequest tokenRefreshRequest) {

        tokenService.verifyAndValidate(tokenRefreshRequest.getRefreshToken());

        final String adminId = tokenService
                .getPayload(tokenRefreshRequest.getRefreshToken())
                .get(TokenClaims.USER_ID.getValue())
                .toString();

        final AdminEntity adminEntityFromDB = adminRepository
                .findById(adminId)
                .orElseThrow(AdminNotFoundException::new);

        this.validateAdminStatus(adminEntityFromDB);

        return tokenService.generateToken(
                adminEntityFromDB.getClaims(),
                tokenRefreshRequest.getRefreshToken()
        );

    }

    private void validateAdminStatus(final AdminEntity adminEntity) {
        if (!(UserStatus.ACTIVE.equals(adminEntity.getUserStatus()))) {
            throw new UserStatusNotValidException("UserStatus = " + adminEntity.getUserStatus());
        }
    }

}

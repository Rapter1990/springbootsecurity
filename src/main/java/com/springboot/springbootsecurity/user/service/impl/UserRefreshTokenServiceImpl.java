package com.springboot.springbootsecurity.user.service.impl;

import com.springboot.springbootsecurity.auth.exception.UserStatusNotValidException;
import com.springboot.springbootsecurity.auth.model.Token;
import com.springboot.springbootsecurity.auth.model.dto.request.TokenRefreshRequest;
import com.springboot.springbootsecurity.auth.model.enums.TokenClaims;
import com.springboot.springbootsecurity.auth.model.enums.UserStatus;
import com.springboot.springbootsecurity.auth.service.TokenService;
import com.springboot.springbootsecurity.user.exception.UserNotFoundException;
import com.springboot.springbootsecurity.user.model.entity.UserEntity;
import com.springboot.springbootsecurity.user.repository.UserRepository;
import com.springboot.springbootsecurity.user.service.UserRefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRefreshTokenServiceImpl implements UserRefreshTokenService {

    private final UserRepository userRepository;

    private final TokenService tokenService;

    @Override
    public Token refreshToken(TokenRefreshRequest tokenRefreshRequest) {

        tokenService.verifyAndValidate(tokenRefreshRequest.getRefreshToken());

        final String userId = tokenService
                .getPayload(tokenRefreshRequest.getRefreshToken())
                .get(TokenClaims.USER_ID.getValue())
                .toString();

        final UserEntity userEntityFromDB = userRepository
                .findById(userId)
                .orElseThrow(UserNotFoundException::new);

        this.validateUserStatus(userEntityFromDB);

        return tokenService.generateToken(
                userEntityFromDB.getClaims(),
                tokenRefreshRequest.getRefreshToken()
        );

    }

    private void validateUserStatus(final UserEntity userEntity) {
        if (!(UserStatus.ACTIVE.equals(userEntity.getUserStatus()))) {
            throw new UserStatusNotValidException("UserStatus = " + userEntity.getUserStatus());
        }
    }

}

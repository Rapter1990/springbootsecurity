package com.springboot.springbootsecurity.user.service;

import com.springboot.springbootsecurity.auth.model.Token;
import com.springboot.springbootsecurity.auth.model.dto.request.TokenRefreshRequest;

public interface UserRefreshTokenService {

    Token refreshToken(final TokenRefreshRequest tokenRefreshRequest);

}

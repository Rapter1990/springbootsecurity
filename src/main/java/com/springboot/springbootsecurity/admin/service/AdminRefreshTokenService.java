package com.springboot.springbootsecurity.admin.service;

import com.springboot.springbootsecurity.auth.model.Token;
import com.springboot.springbootsecurity.auth.model.dto.request.TokenRefreshRequest;

public interface AdminRefreshTokenService {

    Token refreshToken(final TokenRefreshRequest tokenRefreshRequest);

}

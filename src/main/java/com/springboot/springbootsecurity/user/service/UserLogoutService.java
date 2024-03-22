package com.springboot.springbootsecurity.user.service;

import com.springboot.springbootsecurity.auth.model.dto.request.TokenInvalidateRequest;

public interface UserLogoutService {

    void logout(final TokenInvalidateRequest tokenInvalidateRequest);

}
